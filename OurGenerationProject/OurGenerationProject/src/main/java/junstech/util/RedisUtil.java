package junstech.util;

import redis.clients.jedis.Jedis;
public class RedisUtil {
   private static Jedis jedis = null;
   
   public static Jedis getJedis(){
	   if(jedis == null){
		   jedis = new Jedis("localhost");
	   }
	   return jedis;
   }
	
   public static String setString(String key, String value){
	   return getJedis().set(key, value);
   }
   
   public static String getString(String key){
	   return getJedis().get(key);
   }
   
   public static boolean contains(String key){
	   return getJedis().exists(key);
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

