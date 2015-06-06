package edu.hrbeu.myweather;

import javax.crypto.Mac;

import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

public class EncodeUtil {

	public String url;
	private static final char last2byte = (char) Integer
			.parseInt("00000011", 2);
	private static final char last4byte = (char) Integer
			.parseInt("00001111", 2);
	private static final char last6byte = (char) Integer
			.parseInt("00111111", 2);
	private static final char lead6byte = (char) Integer
			.parseInt("11111100", 2);
	private static final char lead4byte = (char) Integer
			.parseInt("11110000", 2);
	private static final char lead2byte = (char) Integer
			.parseInt("11000000", 2);
	private static final char[] encodeTable = new char[] { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };

	public static String standardURLEncoder(String data, String key) {
		byte[] byteHMAC = null;
		String urlEncoder = "";
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
			if (byteHMAC != null) {
				String oauth = encode(byteHMAC);
				if (oauth != null) {
					urlEncoder = URLEncoder.encode(oauth, "utf8");
				}
			}
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return urlEncoder;
	}

	public static String encode(byte[] from) {
		StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
		int num = 0;
		char currentByte = 0;
		for (int i = 0; i < from.length; i++) {
			num = num % 8;
			while (num < 8) {
				switch (num) {
				case 0:
					currentByte = (char) (from[i] & lead6byte);
					currentByte = (char) (currentByte >>> 2);
					break;
				case 2:
					currentByte = (char) (from[i] & last6byte);
					break;
				case 4:
					currentByte = (char) (from[i] & last4byte);
					currentByte = (char) (currentByte << 2);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead2byte) >>> 6;
					}
					break;
				case 6:
					currentByte = (char) (from[i] & last2byte);
					currentByte = (char) (currentByte << 4);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead4byte) >>> 4;
					}
					break;
				}
				to.append(encodeTable[currentByte]);
				num += 6;
			}
		}
		if (to.length() % 4 != 0) {
			for (int i = 4 - to.length() % 4; i > 0; i--) {
				to.append("=");
			}
		}
		return to.toString();
	}

	public EncodeUtil() {
		try {
			String areaid = "101010100";
			String type = "forecast_v";
			String appid = "c2ffc8e63c5b40ca";
			String appid_six = "c2ffc8";
			String private_key = "0244f8_SmartWeatherAPI_5e9551e";
			
			Date dt=new Date();//�������Ҫ��ʽ,��ֱ����dt,dt���ǵ�ǰϵͳʱ��
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//������ʾ��ʽ
			String nowTime="";
			nowTime= df.format(dt);//��DateFormat��format()������dt�л�ȡ����yyyy/MM/dd HH:mm:ss��ʽ��ʾ
			System.out.println("nowTime:"+nowTime);
			
			
			// ��Ҫ���ܵ�����
			String public_key = "http://open.weather.com.cn/data/?areaid="+areaid+"&type="+type+"&date="+nowTime+"&appid="+appid;
			// ��Կ
		//	String key = "0244f8_SmartWeatherAPI_5e9551e";

			String key = standardURLEncoder(public_key, private_key);
//			
//			String data2 = "http://open.weather.com.cn/data/?areaid="+areaid+"&type="+type+"&date="+nowTime+"&appid="+appid_six;
//			
//			System.out.println(str);
			url="http://open.weather.com.cn/data/?areaid="+areaid+"&type="+type+"&date="+nowTime+"&appid="+appid_six+"&key="+key;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// AppId��c2ffc8e63c5b40ca
	// Private_Key��0244f8_SmartWeatherAPI_5e9551e
//	lWTMWq3QRJyLstvvvv6So0DBkQ0%3D
//	hPZf1%2F5Ag%2F4JS2A%2BcYmpj5Yv2YA%3D
}