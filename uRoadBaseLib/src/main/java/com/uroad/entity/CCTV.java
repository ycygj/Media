package com.uroad.entity;

/**
 * @author Administrator
 *
 */
public class CCTV {

	private String poiId;
	private String roadId;
	private String poiName;
	private String roadName;
	private double lon;
	private double lat;
	private String imageurl;
	private String imageurl2;
	private Object tag;//冗余字段，
	private String imageurl3;
	private String modified;
	private boolean isfav;
	private boolean isbeak; //是否坏了

	/**
	 * @return the tag
	 */
	public Object getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(Object tag) {
		this.tag = tag;
	}


	/**
	 * @return the imageurl2
	 */
	public String getImageurl2() {
		return imageurl2;
	}

	/**
	 * @param imageurl2 the imageurl2 to set
	 */
	public void setImageurl2(String imageurl2) {
		this.imageurl2 = imageurl2;
	}

	/**
	 * @return the imageurl3
	 */
	public String getImageurl3() {
		return imageurl3;
	}

	/**
	 * @param imageurl3 the imageurl3 to set
	 */
	public void setImageurl3(String imageurl3) {
		this.imageurl3 = imageurl3;
	}


	/**
	 * @return the roadId
	 */
	public String getRoadId() {
		return roadId;
	}

	/**
	 * @param roadId the roadId to set
	 */
	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}


	/**
	 * @return the imageurl
	 */
	public String getImageurl() {
		return imageurl;
	}

	/**
	 * @param imageurl
	 *            the imageurl to set
	 */
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * @param lon
	 *            the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getPoiId() {
		return poiId;
	}

	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	/**
	 * @return the isfav
	 */
	public boolean getIsfav() {
		return isfav;
	}

	/**
	 * @param isfav the isfav to set
	 */
	public void setIsfav(boolean isfav) {
		this.isfav = isfav;
	}

	/**
	 * @return the isbeak
	 */
	public boolean getIsbeak() {
		return isbeak;
	}

	/**
	 * @param isbeak the isbeak to set
	 */
	public void setIsbeak(boolean isbeak) {
		this.isbeak = isbeak;
	}


}
