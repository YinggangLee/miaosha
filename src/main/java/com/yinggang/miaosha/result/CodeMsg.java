package com.yinggang.miaosha.result;

public class CodeMsg {
	
	private int code;
	private String msg;
	
	//ͨ�õĴ�����
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "������쳣");
	public static CodeMsg BIND_ERROR = new CodeMsg(500101, "����У���쳣��%s");
	//��¼ģ�� 5002XX
	public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session�����ڻ����Ѿ�ʧЧ");
	public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "��¼���벻��Ϊ��");
	public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "�ֻ��Ų���Ϊ��");
	public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "�ֻ��Ÿ�ʽ����");
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "�ֻ��Ų�����");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "�������");
	
	//��Ʒģ�� 5003XX
	
	//����ģ�� 5004XX
	
	//��ɱģ�� 5005XX
	public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "��Ʒ�Ѿ���ɱ���");
	public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "�����ظ���ɱ");
	public static CodeMsg MIAOSHA_ING = new CodeMsg(500502, "��ɱ������");


	public static CodeMsg RESET = new CodeMsg(500509, "reset");

	
	private CodeMsg( ) {
	}
			
	private CodeMsg( int code,String msg ) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code, message);
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
