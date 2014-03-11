package dataModel;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import processor.LocationRecognizer;

import entity.CheckIn;
import entity.FoursquareCategory;
import entity.FoursquareLocation;

public class GeoQuery {

	public static double pre_latitude = -0.1;
	public static double pre_longitude = -0.1;
	public static int pre_locationId = -1;
	public static DetailedLocation pre_detailLedocation = null;
	
	

	private static int sleepTime = 500;

	public static String executePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			// System.out.println(response.toString());
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static String executeHttpsRequest(String targetURL,String urlParameters) 
	{
		String response = "";
		try {
            URL url = new URL(targetURL);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            
            X509TrustManager xtm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }
                
                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub
                    
                }
                
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub
                    
                }
            };
            
            TrustManager[] tm = { xtm };
            
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);
            
            con.setSSLSocketFactory(ctx.getSocketFactory());
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            
//            System.out.println(con.getResponseCode());
            if (con.getResponseCode() == 403) return "403_ERROR";
            if (con.getResponseCode() == 400) return "400_ERROR";
            InputStreamReader insr = new InputStreamReader(con.getInputStream(),"UTF-8");
            // 读取服务器的响应内容并显示
            
            int respInt = insr.read();
            while (respInt != -1) {
//                System.out.print((char) respInt);
                response += (char) respInt;
                respInt = insr.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
		return response;
	}
	
	static void log(Object o) {
        System.out.println(o);
    }

	public static FoursquareLocation verifyLocation(CheckIn checkIn, String foursquareKey) {
//		try {
//			Thread.sleep(sleepTime);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String locationQuery = LocationRecognizer.filterPlaceByPtn(checkIn.getTweet().getContent());
		if (locationQuery.equals("NOT_FOUND")) return new FoursquareLocation("NOT_FOUND", "", new LinkedList<FoursquareCategory>(), false);
		String requestStr = "https://api.foursquare.com/v2/venues/search?ll=" + 
							checkIn.getCoordinate().getLatitude() + "," + 
							checkIn.getCoordinate().getLongitude() +"&query=" +
							encodeBlank(locationQuery) + 
//							"&oauth_token=QJH3VZ5YQJT3HES5R1MTM0RFFFI2GP13MHDHHKKX3F0HJLJ4&v=20140102";//163 key
							"&oauth_token=" + foursquareKey +
							"&v=20140102";//gmail key
		String responseStr = executeHttpsRequest(requestStr,"");
		if (responseStr.equals("403_ERROR"))
			return new FoursquareLocation("403_ERROR", "", new LinkedList<FoursquareCategory>(), false);
		if (responseStr.equals("400_ERROR"))
			return new FoursquareLocation("400_ERROR", "", new LinkedList<FoursquareCategory>(), false);
		return unpackFoursquareResponseJson(responseStr);
	}

	public static DetailedLocation getDetailedLocationFromGoogle(CheckIn checkIn) {

		DetailedLocation dl;
		if (skipRequestAPI(checkIn)) {
			dl = pre_detailLedocation;
		} else {
			System.out.println("new request");
			try {
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String requestStr = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
					+ checkIn.getCoordinate().getLatitude()
					+ ","
					+ checkIn.getCoordinate().getLongitude() + "&sensor=true";
			dl = pre_detailLedocation = unpackGoogleResponseJson(executePost(
					requestStr, ""));
		}

		pre_latitude = checkIn.getCoordinate().getLatitude();
		pre_longitude = checkIn.getCoordinate().getLongitude();
		return pre_detailLedocation;
	}

	private static boolean skipRequestAPI(CheckIn checkIn) {
		return false;
	}

	// private static boolean skipRequestAPI(CheckIn checkIn)
	// {
	// if (checkIn.getLocation().getId() == pre_locationId)
	// return true;
	// else if (Math.abs(checkIn.getCoordinate().getLatitude()-pre_latitude) <
	// 0.1 &&
	// Math.abs(checkIn.getCoordinate().getLongitude()-pre_longitude) < 0.1)
	// return true;
	// else
	// return false;
	// }

	private static FoursquareLocation unpackFoursquareResponseJson(String jsonString) 
	{
		String id = "", name = "";
		List<FoursquareCategory> categoryList = new LinkedList<FoursquareCategory>();
		boolean verified = false;
		try {
			if (jsonString.equals(""))
				return new FoursquareLocation("", "",
						new LinkedList<FoursquareCategory>(), false);
			JSONObject root = new JSONObject(jsonString);
			JSONObject metaObj = (JSONObject) root.get("meta");
			if (!metaObj.get("code").toString().equals("200")) {
				System.out.println("PARA_ERROR");
				return new FoursquareLocation("PARA_ERROR", "",
						new LinkedList<FoursquareCategory>(), false);
			}
			JSONObject responseObj = (JSONObject) root.get("response");
			JSONArray venues = (JSONArray) responseObj.get("venues");
			if (venues.length() == 0) {
				System.out.println("VENUE_EMPTY");
				return new FoursquareLocation("VENUE_EMPTY", "",
						new LinkedList<FoursquareCategory>(), false);
			}

			// 取第一个结果（匹配的最好的）
			JSONObject firstVenueObj = venues.getJSONObject(0);
			id = firstVenueObj.get("id").toString();
			name = firstVenueObj.get("name").toString();

			// 读取分类信息
			JSONArray categoryArr = (JSONArray) firstVenueObj.get("categories");
			for (int i = 0; i < categoryArr.length(); i++) {
				JSONObject cat = categoryArr.getJSONObject(i);
				FoursquareCategory focat = new FoursquareCategory(cat.get("id")
						.toString(), cat.get("name").toString());
				categoryList.add(focat);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new FoursquareLocation(id, name, categoryList, true);
	}

	private static DetailedLocation unpackGoogleResponseJson(String jsonString) {

		String country = "", admin_level_1 = "", admin_level_2 = "";

		try {
			JSONObject obj = new JSONObject(jsonString);

			if (obj.get("status").toString().equals("ZERO_RESULTS")) {
				System.out.println("BAD COORDINATE");
				return new DetailedLocation("", "", "");
			}
			JSONArray resList = (JSONArray) obj.get("results");
			if (resList == null) {
				System.out.println("SERVER NOT RESPONSE");
				return new DetailedLocation("", "", "");
			}
			JSONObject obj1 = resList.getJSONObject(0);
			JSONArray addrList = (JSONArray) obj1.get("address_components");
			for (int i = 0; i < addrList.length(); i++) {
				JSONObject obj2 = addrList.getJSONObject(i);
				JSONArray typeList = (JSONArray) obj2.get("types");
				for (int j = 0; j < typeList.length(); j++) {
					if (typeList.getString(0).equals("country")) {
						country = obj2.get("long_name").toString();
					}
					if (typeList.getString(0).equals(
							"administrative_area_level_1")) {
						admin_level_1 = obj2.get("long_name").toString();
					}
					if (typeList.getString(0).equals(
							"administrative_area_level_2")) {
						admin_level_2 = obj2.get("long_name").toString();
					}
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(country+", "+admin_level_1+", "+admin_level_2);
		DetailedLocation detailLocation = new DetailedLocation(country,
				admin_level_1, admin_level_2);
		return detailLocation;
	}
	
	private static String encodeBlank(String in)
	{
		return in.replaceAll(" ", "%20");
	}

	public static DetailedLocation filterNewYork(CheckIn checkIn) {
		DetailedLocation detailedLocation;
		if (checkIn.getCoordinate().getLatitude() > 40.42
				&& checkIn.getCoordinate().getLatitude() < 41.16
				&& checkIn.getCoordinate().getLongitude() > -74.72
				&& checkIn.getCoordinate().getLongitude() < -73.24)
			detailedLocation = new DetailedLocation("United States",
					"New York", "");
		else
			detailedLocation = new DetailedLocation("Undetermined",
					"Undetermined", "Undetermined");
		return detailedLocation;
	}

	public static DetailedLocation filterFrasco(CheckIn checkIn) {
		DetailedLocation detailedLocation;
		if (checkIn.getCoordinate().getLatitude() > 37.70
				&& checkIn.getCoordinate().getLatitude() < 37.84
				&& checkIn.getCoordinate().getLongitude() > -122.55
				&& checkIn.getCoordinate().getLongitude() < -122.33)
			detailedLocation = new DetailedLocation("United States",
					"San Francisco", "");
		else
			detailedLocation = new DetailedLocation("Undetermined",
					"Undetermined", "Undetermined");
		return detailedLocation;
	}

	public static DetailedLocation getDetailedLocationFromFoursquare(
			CheckIn checkIn) {
		return null;
	}
}
