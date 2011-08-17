package com.inter6.pknu.domitorycarte.data;

public final class Const {
	public static final String TODAY_CARTE_URL = "http://dormitory.pknu.ac.kr/";
	
	/**
	 * 0��° : ��¥ ���̺� ��ȣ
	 * 1��° : ��ħ ���̺� ��ȣ
	 * 2��° : ���� ���̺� ��ȣ
	 * 3���� : ���� ���̺� ��ȣ
	 */
	public static final int[] TODAY_CARTE_TABLE_INDEX_LIST_D = {30, 35, 37, 39};
	public static final int[] TODAY_CARTE_TABLE_INDEX_LIST_Y = {30, 44, 46, 48};
	
	public static final String MONTHLY_CARTE_URL_D = "http://dormitory.pknu.ac.kr/?page=life/life_02&category=1";
	public static final String MONTHLY_CARTE_URL_Y = "http://dormitory.pknu.ac.kr/?page=life/life_02&category=3";
	
	/**
	 * 0��° : ��/���� ���̺� ��ȣ
	 * 1��° : �޴��� ���̺� ��ȣ
	 */
	public static final int[] MONTHLY_CARTE_TABLE_INDEX_LIST = {15, 16};
	
	public static final int WHERE_D = 0;
	public static final int WHERE_Y = 1;
	
	public static final int WHEN_BREAKFAST = 0;
	public static final int WHEN_LUNCH = 1;
	public static final int WHEN_DINNER = 2;
}