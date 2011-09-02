package com.inter6.pknu.domitorycarte2.data;

import android.widget.Toast;


public final class Const {
	public static final String TODAY_CARTE_URL = "http://dormitory.pknu.ac.kr/";
	
	/**
	 * 0번째 : 날짜 테이블 번호
	 * 1번째 : 아침 테이블 번호
	 * 2번째 : 점심 테이블 번호
	 * 3번재 : 저녁 테이블 번호
	 */
	public static final int[] TODAY_CARTE_TABLE_INDEX_LIST_D = {30, 35, 37, 39};
	public static final int[] TODAY_CARTE_TABLE_INDEX_LIST_Y = {30, 44, 46, 48};
	
	public static final String WEEKLY_CARTE_URL_D = "http://dormitory.pknu.ac.kr/?page=life/life_02&category=1";
	public static final String WEEKLY_CARTE_URL_Y = "http://dormitory.pknu.ac.kr/?page=life/life_02&category=3";
	
	/**
	 * 0번째 : 월/주차 테이블 번호
	 * 1번째 : 메뉴판 테이블 번호
	 */
	public static final int[] MONTHLY_CARTE_TABLE_INDEX_LIST = {15, 16};
	
	public static final int TODAY_CARTE = 0;
	public static final int WEEKLY_CARTE = 1;
	
	public static final int WHERE_D = 0;
	public static final int WHERE_Y = 1;
	
	public static final int WHEN_BREAKFAST = 0;
	public static final int WHEN_LUNCH = 1;
	public static final int WHEN_DINNER = 2;
	
	public static final int TOAST_DUR = Toast.LENGTH_SHORT;
}