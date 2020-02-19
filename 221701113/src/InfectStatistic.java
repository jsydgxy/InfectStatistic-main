import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;


/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */



class InfectStatistic {
	public static ArrayList<String> fileContent;    //保存从文件中读取的内容
	public static Map<String , String> statistic;    //保存统计的结果
	public static Map<String, Object> sortMap;  //用于保存经过排序的统计结果
	public static File fileArray[];          //保存所有日志文件的路径
	
    public static void main( String[] args) {
    	String filePath=args[6];
    	
    

        FileOutputStream fout = new FileOutputStream(filePath);//输出文件设置
        OutputStreamWriter writer = new OutputStreamWriter(fout);
        BufferedWriter buffWriter = new BufferedWriter(writer);
        
        File file = new File(args[4]);
		fileArray = file.listFiles();
		statistic = new HashMap<String,String>();
	    
		for(int i=0; i < fileArray.length; i++)
		{
			fileContent = new ArrayList<String>();
			try 
			{
				Scanner sc  = new Scanner(fileArray[i]);
				while(sc.hasNext())
				{
					String str = sc.next();
					if (str.equals("//"))
						break;
					
					else fileContent.add(str);
				}					      
				sc.close();
			} 
			
			catch (Exception e) 
			{
				
			}
			getStatistic();//进行文本内容处理
			if(fileArray[i].getName().equals(args[2] + ".log.txt")) break; //结束统计判断
		}			
        buffWriter.write("全国  感染患者" + statistic.get("全国" + "感染患者") + "人" + " "
                + "疑似患者" + statistic.get("全国" + "疑似患者") + "人" + " "
		          + "治愈" + statistic.get("全国" + "治愈") + "人" + " "
                + "死亡" + statistic.get("全国" + "死亡") + "人\n");
        int i = 0;
		for(String key : sortMap.keySet())
		{
			if(i % 4 == 0) //每4个记录是一个省份的内容
			{
				String province = key.substring(0 , key.length() - 4); //将省份提取出来
				buffWriter.write(province + " " + "感染患者" + statistic.get(province + "感染患者")
				+ "人" + " "+ "疑似患者" + statistic.get(province + "疑似患者") + "人" + " "
				          + "治愈" + statistic.get(province + "治愈") + "人" + " "
	                    + "死亡" + statistic.get(province + "死亡") + "人\n");
			}
			i++;			
		}
        buffWriter.close();
     }
    
        public static void getStatistic()
    	/*
    	 * 该方法用于计算统计一个日志文件的各项数据
    	 */
    	{
    		for(int i = 0; i < fileContent.size() - 2; i++)
    		{
    			if(fileContent.get(i + 1).equals("新增") )  //判别新增
    			{
    				String provin = fileContent.get(i);  //获取这条信息关联的省份
    	    		String type = fileContent.get(i + 2); //感染患者或者疑似患者
    	    		String str = fileContent.get(i + 3); ;
    	    		str = str.substring(0 , str.length() - 1); //截取人数
    	    		
    	    		if(!statistic.containsKey(provin + type)) //检查哈希表中是否已经存在该省份的数据了
    	    		{
    	    			initStatistic(provin);
    	    		}

    	    		int sum = Integer.parseInt(str) + Integer.parseInt(statistic.get(provin + type));
    	    		
    	    		statistic.put(provin + type , String.valueOf(sum));
    			}
    			
    			else if(fileContent.get(i + 2).equals("流入")) //判别为流入的情况
    			{
    				String provinO = fileContent.get(i);  //获取有患者流出的省份
    	    		String provinI = fileContent.get(i + 3);  //获取有患者流入的省份
    	    		String type = fileContent.get(i + 1); //感染患者或者疑似患者
    	    		String str = fileContent.get(i + 4);
    	    		str = str.substring(0 , str.length() - 1); //截取人数
    	    		
    	    		if(!statistic.containsKey(provinI + type))  //检查哈希表中是否已经存在该省份的数据了
    	    		{
    	    			initStatistic(provinI);
    	    		}
    	    		
    	    		if(!statistic.containsKey(provinO + type)) 
    	    		{
    	    			initStatistic(provinO);
    	    		}
    	    		
    	    		
    	    		int sum1 = Integer.parseInt(str) + Integer.parseInt(statistic.get(provinI + type)); //统计有患者流入的省份
    	    		int sum2 = Integer.parseInt(statistic.get(provinO + type)) - Integer.parseInt(str); //统计有患者流出的省份
    	    		
    	    		statistic.put(provinI+ type , String.valueOf(sum1));
    	    		statistic.put(provinO + type , String.valueOf(sum2));
    			}
    			
    			else if(fileContent.get(i + 1).equals("死亡") || fileContent.get(i + 1).equals("治愈")) //判别为死亡或者治愈的情况
    			{
    				String provin = fileContent.get(i);  //获取数据相关的省份
    	    		String type = fileContent.get(i + 1); //获取是治愈了还是死亡了
    	    		String str = fileContent.get(i + 2);
    	    		str = str.substring(0 , str.length() - 1); //截取人数
    	    		
    	    		if(!statistic.containsKey(provin + type))  //检查哈希表中是否已经存在该省份的数据了
    	    		{
    	    			initStatistic(provin);
    	    		}
    	    				
    	    		int sum = Integer.parseInt(str) + Integer.parseInt(statistic.get(provin + type));			
    	    		int infect = Integer.parseInt(statistic.get(provin + "感染患者")) - Integer.parseInt(str);  //无论是治愈了还是死亡了，感染患者现存数都要减去
    	    		
    	    		statistic.put(provin + type , String.valueOf(sum));
    	    		statistic.put(provin + "感染患者" , String.valueOf(infect));
    			}
    			
    			else if(fileContent.get(i + 2).equals("确诊感染") || fileContent.get(i + 1).equals("排除")) //判别为确诊感染或排除的情况
    			{
    				String provin = fileContent.get(i);  //获取数据相关的省份
    	    		String str = fileContent.get(i + 3);
    	    		str = str.substring(0 , str.length() - 1); //截取人数
    	    		
    	    		if(!statistic.containsKey(provin + "疑似患者"))  //检查哈希表中是否已经存在该省份的数据了
    	    		{
    	    			initStatistic(provin);
    	    		}
    	    		
    	    		int infect = 0;
    	    		int unknownInfect = 0;
    	    		
    	    		if(fileContent.get(i + 2).equals("确诊感染")) //如果是确认感染，那感染患者数量要加上
    	    		{
    	    			infect = Integer.parseInt(statistic.get(provin + "感染患者")) + Integer.parseInt(str);
    	    			statistic.put(provin + "感染患者" , String.valueOf(infect));
    	    		}
    	    		

    	    		unknownInfect = Integer.parseInt(statistic.get(provin + "疑似患者")) - Integer.parseInt(str);  //无论是排除还是确诊，疑似患者现存数都要减去
    	    		
    	    		statistic.put(provin + "疑似患者" , String.valueOf(unknownInfect));
    			}
    			
    		}
    	}
        
        public static void initStatistic(String provin)
    	/*
    	 * 该方法用于初始化统计数据的哈希表，如果日志中出现了该省的信息，则进行初始化
    	 */
    	{
    		statistic.put(provin + "感染患者", "0");  //如果哈希表中并没有存放该省份，就初始化该省份
    		statistic.put(provin + "疑似患者", "0");
    		statistic.put(provin + "治愈", "0");
    		statistic.put(provin + "死亡", "0");
    	}		
        
        public static void countryStatic()
        /*
         *  该方法用来统计全国的疫情数据
         */
    	{
    		int infect = 0;
    		int unknownInfect = 0;
    		int cure = 0;
    		int dead = 0;
    		for (String key : sortMap.keySet()) 
    		{
    			int num = Integer.parseInt(sortMap.get(key).toString());
    			if(key.substring(key.length()- 4, key.length()).equals("感染患者"))
    			{
    				infect += num;
    			}
    			else if(key.substring(key.length()- 4, key.length()).equals("疑似患者"))
    			{
    				unknownInfect += num;
    			}
    			else if(key.substring(key.length()- 2, key.length()).equals("治愈"))
    			{
    				cure += num;
    			}
    			else if(key.substring(key.length()- 2, key.length()).equals("死亡"))
    			{
    				dead += num;
    			}			
    		}
    		
    		statistic.put("全国感染患者", String.valueOf(infect));
    		statistic.put("全国疑似患者", String.valueOf(unknownInfect));
    		statistic.put("全国治愈", String.valueOf(cure));
    		statistic.put("全国死亡", String.valueOf(dead));
    	}
        
    	
    	
    	
   

    
}
