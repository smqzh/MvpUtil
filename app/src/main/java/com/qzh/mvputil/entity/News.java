package com.qzh.mvputil.entity;

/**
 * Created by lcx on 2017/6/5.
 */

public class News {

	/**
	 * id : wechat_20150401071581
	 * title : 号外：集宁到乌兰花的班车出事了！！！！！
	 * source : 内蒙那点事儿
	 * firstImg : http://zxpic.gtimg.com/infonew/0/wechat_pics_-214279.jpg/168
	 * mark :
	 * url : http://v.juhe.cn/weixin/redirect?wid=wechat_20150401071581
	 */

	private String id;
	private String title;
	private String source;
	private String firstImg;
	private String mark;
	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getFirstImg() {
		return firstImg;
	}

	public void setFirstImg(String firstImg) {
		this.firstImg = firstImg;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public News(String id, String title, String source, String firstImg, String mark, String url) {
		this.id = id;
		this.title = title;
		this.source = source;
		this.firstImg = firstImg;
		this.mark = mark;
		this.url = url;
	}

	public News() {
	}
}
