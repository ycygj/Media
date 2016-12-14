package com.uroad.widget.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.uroad.util.ImageUtil;

@SuppressLint("FloatMath")
public class ImageViewTouchBase extends ImageView {

	protected float MAX_SCALE = 8.0f;
	float imageW;
	float imageH;
	float rotatedImageW;
	float rotatedImageH;
	float viewW;
	float viewH;
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	static final int NONE = 0;// 闁跨喐鏋婚幏宄邦瀶閻樿鎷�
	static final int DRAG = 1;// 闁跨喕绶濈拋瑙勫
	static final int ZOOM = 2;// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
	static final int ROTATE = 3;// 闁跨喐鏋婚幏鐤祮
	static final int ZOOM_OR_ROTATE = 4; // 闁跨喐鏋婚幏鐑芥晸閼存矮绱幏鐑芥晸閺傘倖瀚归敓锟�
	protected boolean mScaleEnabled = true;
	protected boolean mRotateEnabled = false;
	protected boolean mTouchEnabled = true;
	int mode = NONE;
	Context mContext;
	PointF pA = new PointF();
	PointF pB = new PointF();
	PointF mid = new PointF();
	PointF lastClickPos = new PointF();
	float minScale = 1f;
	long lastClickTime = 0;
	double rotation = 0.0;
	float dist = 1f;
	boolean isCircle = false;

	public boolean onLeftSide = false, onTopSide = false, onRightSide = false,
			onBottomSide = false;

	public ImageViewTouchBase(Context context) {
		super(context);
		mContext = context;
		init(null);
	}

	public ImageViewTouchBase(Context context, float maxscale) {
		super(context);
		mContext = context;
		this.MAX_SCALE = maxscale;
		init(null);
	}

	public void setScaleEnabled(boolean e) {
		mScaleEnabled = e;
		mRotateEnabled = e;
		mTouchEnabled = e;// 閸戦缚顢戦弰锟芥＃鏍�閺堝鏁ら崚鎷岀箹娑擄拷
	}

	public boolean getScaleEnabled() {
		return mScaleEnabled;
	}

	public ImageViewTouchBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs);
	}

	public ImageViewTouchBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		setScaleType(ImageView.ScaleType.MATRIX);
	}

	public void setBaseScaleType(ImageView.ScaleType type) {
		setScaleType(type);
		invalidate();
	}

	public void resetScale() {

	}

	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		setImageWidthHeight();
	}

	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		setImageWidthHeight();
	}

	public void setImageResource(int resId) {
		super.setImageResource(resId);
		setImageWidthHeight();
	}

	private void setImageWidthHeight() {
		Drawable d = getDrawable();
		if (d == null) {
			return;
		}
		imageW = rotatedImageW = d.getIntrinsicWidth();
		imageH = rotatedImageH = d.getIntrinsicHeight();
		initImage();
	}

	public boolean pagerCanScroll() {
		if (mode != NONE)
			return false;

		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		return curScale >= minScale && curScale <= 1.072f;
	}

	private void checkSiding() {
		if (pagerCanScroll()) {
			return;
		}
		float m[] = new float[9];
		matrix.getValues(m);
		float matrixX = m[Matrix.MTRANS_X];
		float matrixY = m[Matrix.MTRANS_Y];
		float curScale = Math.abs(m[0]) + Math.abs(m[1]);
		// Log.d(TAG, "x: " + matrixX + " y: " + matrixY + " left: " + right / 2
		// + " top:" + bottom / 2);
		float scaleWidth = Math.round(imageW * curScale);
		float scaleHeight = Math.round(imageH * curScale);
		onLeftSide = onRightSide = onTopSide = onBottomSide = false;
		if (-matrixX < 10.0f)
			onLeftSide = true;
		// Log.d("GalleryViewPager",
		// String.format("ScaleW: %f; W: %f, MatrixX: %f", scaleWidth, width,
		// matrixX));
		if ((scaleWidth >= viewW && (matrixX + scaleWidth - viewW) < 10)
				|| (scaleWidth <= viewW && -matrixX + scaleWidth <= viewW))
			onRightSide = true;
		if (-matrixY < 10.0f)
			onTopSide = true;
		if (Math.abs(-matrixY + viewH - scaleHeight) < 10.0f)
			onBottomSide = true;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewW = w;
		viewH = h;
		if (oldw == 0) {
			initImage();
		} else {
			fixScale();
			fixTranslation();
			setImageMatrix(matrix);
		}
	}

	private void initImage() {
		if (viewW <= 0 || viewH <= 0 || imageW <= 0 || imageH <= 0) {
			return;
		}
		mode = NONE;
		matrix.setScale(0, 0);
		fixScale();
		fixTranslation();
		setImageMatrix(matrix);
	}

	private void fixScale() {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min((float) viewW / (float) rotatedImageW,
				(float) viewH / (float) rotatedImageH);

		if (curScale < minScale) {
			if (curScale > 0) {
				double scale = minScale / curScale;
				p[0] = (float) (p[0] * scale);
				p[1] = (float) (p[1] * scale);
				p[3] = (float) (p[3] * scale);
				p[4] = (float) (p[4] * scale);
				matrix.setValues(p);
			} else {
				matrix.setScale(minScale, minScale);
			}
		}
	}

	private float maxPostScale() {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min((float) viewW / (float) rotatedImageW,
				(float) viewH / (float) rotatedImageH);
		float maxScale = Math.max(minScale, MAX_SCALE);
		return maxScale / curScale;
	}

	private void fixTranslation() {
		RectF rect = new RectF(0, 0, imageW, imageH);
		matrix.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (width < viewW) {
			deltaX = (viewW - width) / 2 - rect.left;
		} else if (rect.left > 0) {
			deltaX = -rect.left;
		} else if (rect.right < viewW) {
			deltaX = viewW - rect.right;
		}

		if (height < viewH) {
			deltaY = (viewH - height) / 2 - rect.top;
		} else if (rect.top > 0) {
			deltaY = -rect.top;
		} else if (rect.bottom < viewH) {
			deltaY = viewH - rect.bottom;
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 闁跨喐鏋婚幏鐑芥晸婵劖瀵滈柨鐔告灮閿燂拷
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			pA.set(event.getX(), event.getY());
			pB.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		// 闁跨喐鏋婚幏鐑芥晸婵劖瀵滈柨鐔告灮閿燂拷
		case MotionEvent.ACTION_POINTER_DOWN:
			if (event.getActionIndex() > 1)
				break;
			dist = spacing(event.getX(0), event.getY(0), event.getX(1),
					event.getY(1));
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨噦鎷�闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔峰建鐠佽瀚规稉娲晸閺傘倖瀚归柨鐔惰寧閿涱亷鎷�
			if (dist > 10f) {
				savedMatrix.set(matrix);
				pA.set(event.getX(0), event.getY(0));
				pB.set(event.getX(1), event.getY(1));
				mid.set((event.getX(0) + event.getX(1)) / 2,
						(event.getY(0) + event.getY(1)) / 2);
				mode = ZOOM_OR_ROTATE;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			if (mode == DRAG) {
				if (spacing(pA.x, pA.y, pB.x, pB.y) < 50) {
					long now = System.currentTimeMillis();
					if (now - lastClickTime < 500
							&& spacing(pA.x, pA.y, lastClickPos.x,
									lastClickPos.y) < 50) {
						doubleClick(pA.x, pA.y);
						now = 0;
					}
					lastClickPos.set(pA);
					lastClickTime = now;
				}
			} else if (mode == ROTATE) {
				int level = (int) Math.floor((rotation + Math.PI / 4)
						/ (Math.PI / 2));
				if (level == 4)
					level = 0;
				matrix.set(savedMatrix);
				matrix.postRotate(90 * level, mid.x, mid.y);
				if (level == 1 || level == 3) {
					float tmp = rotatedImageW;
					rotatedImageW = rotatedImageH;
					rotatedImageH = tmp;
					fixScale();
				}
				fixTranslation();
				setImageMatrix(matrix);
			}
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:

			if (mode == ZOOM_OR_ROTATE) {
				PointF pC = new PointF(event.getX(1) - event.getX(0) + pA.x,
						event.getY(1) - event.getY(0) + pA.y);
				double a = spacing(pB.x, pB.y, pC.x, pC.y);
				double b = spacing(pA.x, pA.y, pC.x, pC.y);
				double c = spacing(pA.x, pA.y, pB.x, pB.y);
				if (a >= 10) {
					double cosB = (a * a + c * c - b * b) / (2 * a * c);
					double angleB = Math.acos(cosB);
					double PID4 = Math.PI / 4;
					if (angleB > PID4 && angleB < 3 * PID4) {
						mode = ROTATE;
						rotation = 0;
					} else {
						mode = ZOOM;
					}
				}
			}

			if (mode == DRAG) {
				matrix.set(savedMatrix);
				pB.set(event.getX(), event.getY());
				matrix.postTranslate(event.getX() - pA.x, event.getY() - pA.y);
				fixTranslation();
				setImageMatrix(matrix);
			} else if (mode == ZOOM) {
				float newDist = spacing(event.getX(0), event.getY(0),
						event.getX(1), event.getY(1));
				if (newDist > 10f && mScaleEnabled) {
					matrix.set(savedMatrix);
					float tScale = Math.min(newDist / dist, maxPostScale());
					matrix.postScale(tScale, tScale, mid.x, mid.y);
					fixScale();
					fixTranslation();
					setImageMatrix(matrix);
				}
			} else if (mode == ROTATE) {
				if (mRotateEnabled) {
					PointF pC = new PointF(
							event.getX(1) - event.getX(0) + pA.x, event.getY(1)
									- event.getY(0) + pA.y);
					double a = spacing(pB.x, pB.y, pC.x, pC.y);
					double b = spacing(pA.x, pA.y, pC.x, pC.y);
					double c = spacing(pA.x, pA.y, pB.x, pB.y);
					if (b > 10) {
						double cosA = (b * b + c * c - a * a) / (2 * b * c);
						double angleA = Math.acos(cosA);
						double ta = pB.y - pA.y;
						double tb = pA.x - pB.x;
						double tc = pB.x * pA.y - pA.x * pB.y;
						double td = ta * pC.x + tb * pC.y + tc;
						if (td > 0) {
							angleA = 2 * Math.PI - angleA;
						}
						rotation = angleA;
						matrix.set(savedMatrix);
						matrix.postRotate((float) (rotation * 180 / Math.PI),
								mid.x, mid.y);
						setImageMatrix(matrix);
					}
				}
			}
			break;
		}
		checkSiding();
		return mTouchEnabled;
	}

	public void ZoomTo(float to) {
		if (mScaleEnabled) {
			matrix.postScale(to, to, mid.x, mid.y);
			fixScale();
			fixTranslation();
			setImageMatrix(matrix);
		}
	}

	/**
	 * 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归張銊╂晸閺傘倖瀚归柨鐕傛嫹
	 */
	private float spacing(float x1, float y1, float x2, float y2) {
		float x = x1 - x2;
		float y = y1 - y2;
		return FloatMath.sqrt(x * x + y * y);
	}

	private void doubleClick(float x, float y) {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min((float) viewW / (float) rotatedImageW,
				(float) viewH / (float) rotatedImageH);
		if (curScale <= minScale + 0.01) { // 闁跨喕鍓兼潏鐐
			float toScale = Math.max(minScale, MAX_SCALE) / curScale;
			matrix.postScale(toScale, toScale, x, y);
		} else { // 闁跨喐鏋婚幏宄扮毈
			float toScale = minScale / curScale;
			matrix.postScale(toScale, toScale, x, y);
			fixTranslation();
		}
		setImageMatrix(matrix);
	}

	public void setCircle(boolean c) {
		isCircle = c;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!isCircle) {
			super.onDraw(canvas);
			return;
		}

		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		this.measure(0, 0);
		if (drawable.getClass() == NinePatchDrawable.class)
			return;
		Bitmap b = ImageUtil.drawableToBitmap(drawable);
		if(b!=null){
			Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

			int defaultWidth = getWidth();

			int defaultHeight = getHeight();

			int radius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2;

			Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);
			canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight
					/ 2 - radius, null);			
		}
	}

	/**
	 * 获取裁剪后的圆形图片
	 * 
	 * @param radius
	 *            半径
	 */
	private Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;

		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth) {// 高大于宽
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			// 截取正方形图片
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else if (bmpHeight < bmpWidth) {// 宽大于高
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else {
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter
				|| squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
					diameter, true);

		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		// bitmap回收(recycle导致在布局文件XML看不到效果)
		// bmp.recycle();
		// squareBitmap.recycle();
		// scaledSrcBmp.recycle();
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}

}
