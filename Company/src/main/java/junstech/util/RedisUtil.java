package junstech.util;

import java.util.HashMap;

import redis.clients.jedis.Jedis;
public class RedisUtil {
   private static Jedis jedis = null;
   
   private static HashMap<String, String> text = new HashMap<String, String>();
   
   public static Jedis getJedis(){
	   if(jedis == null){
		   jedis = new Jedis("localhost");
	   }
	   return jedis;
   }
	
   public static void setString(String key, String value){
	   text.put(key, value);
	   getJedis().set(key, value);
   }
   
   public static String getString(String key){
	   if(text.containsKey(key)){
		   return text.get(key);
	   }
	   System.out.println("getFromRedis: " + key);
	   return getJedis().get(key);
   }
   
   public static void main(String[] args) {
      //连接本地的 Redis 服务
      Jedis jedis = new Jedis("localhost");
      System.out.println("Connection to server sucessfully");
      //查看服务是否运行
      System.out.println("Server is running: "+jedis.ping());
      jedis.set("w3ckey", "Redis tutorial");
      // 获取存储的数据并输出
      System.out.println("Stored string in redis:: "+ jedis.get("w3ckey"));
 }
}

