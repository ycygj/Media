package com.uroad.util;

import android.content.Context;

public class ResourceUtils {

	public static int getResourceId(Context ctx, String resourceName) {
		return getId(ctx, "id", resourceName);
	}

	public static int getStringId(Context ctx, String stringName) {
		return getId(ctx, "string", stringName);
	}

	private static int getId(Context ctx, String type, String name) {
		return ctx.getResources().getIdentifier(name, type,
				ctx.getPackageName());
	}

	protected ResourceUtils() {
	}

}

