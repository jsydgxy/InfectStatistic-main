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
	public static ArrayList<String> fileContent;    //������ļ��ж�ȡ������
	public static Map<String , String> statistic;    //����ͳ�ƵĽ��
	public static Map<String, Object> sortMap;  //���ڱ��澭�������ͳ�ƽ��
	public static File fileArray[];          //����������־�ļ���·��
	
    public static void main( String[] args) {
    	String filePath=args[6];
    	
    

        FileOutputStream fout = new FileOutputStream(filePath);//����ļ�����
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
			getStatistic();//�����ı����ݴ���
			if(fileArray[i].getName().equals(args[2] + ".log.txt")) break; //����ͳ���ж�
		}			
        buffWriter.write("ȫ��  ��Ⱦ����" + statistic.get("ȫ��" + "��Ⱦ����") + "��" + " "
                + "���ƻ���" + statistic.get("ȫ��" + "���ƻ���") + "��" + " "
		          + "����" + statistic.get("ȫ��" + "����") + "��" + " "
                + "����" + statistic.get("ȫ��" + "����") + "��\n");
        int i = 0;
		for(String key : sortMap.keySet())
		{
			if(i % 4 == 0) //ÿ4����¼��һ��ʡ�ݵ�����
			{
				String province = key.substring(0 , key.length() - 4); //��ʡ����ȡ����
				buffWriter.write(province + " " + "��Ⱦ����" + statistic.get(province + "��Ⱦ����")
				+ "��" + " "+ "���ƻ���" + statistic.get(province + "���ƻ���") + "��" + " "
				          + "����" + statistic.get(province + "����") + "��" + " "
	                    + "����" + statistic.get(province + "����") + "��\n");
			}
			i++;			
		}
        buffWriter.close();
     }
    
        public static void getStatistic()
    	/*
    	 * �÷������ڼ���ͳ��һ����־�ļ��ĸ�������
    	 */
    	{
    		for(int i = 0; i < fileContent.size() - 2; i++)
    		{
    			if(fileContent.get(i + 1).equals("����") )  //�б�����
    			{
    				String provin = fileContent.get(i);  //��ȡ������Ϣ������ʡ��
    	    		String type = fileContent.get(i + 2); //��Ⱦ���߻������ƻ���
    	    		String str = fileContent.get(i + 3); ;
    	    		str = str.substring(0 , str.length() - 1); //��ȡ����
    	    		
    	    		if(!statistic.containsKey(provin + type)) //����ϣ�����Ƿ��Ѿ����ڸ�ʡ�ݵ�������
    	    		{
    	    			initStatistic(provin);
    	    		}

    	    		int sum = Integer.parseInt(str) + Integer.parseInt(statistic.get(provin + type));
    	    		
    	    		statistic.put(provin + type , String.valueOf(sum));
    			}
    			
    			else if(fileContent.get(i + 2).equals("����")) //�б�Ϊ��������
    			{
    				String provinO = fileContent.get(i);  //��ȡ�л���������ʡ��
    	    		String provinI = fileContent.get(i + 3);  //��ȡ�л��������ʡ��
    	    		String type = fileContent.get(i + 1); //��Ⱦ���߻������ƻ���
    	    		String str = fileContent.get(i + 4);
    	    		str = str.substring(0 , str.length() - 1); //��ȡ����
    	    		
    	    		if(!statistic.containsKey(provinI + type))  //����ϣ�����Ƿ��Ѿ����ڸ�ʡ�ݵ�������
    	    		{
    	    			initStatistic(provinI);
    	    		}
    	    		
    	    		if(!statistic.containsKey(provinO + type)) 
    	    		{
    	    			initStatistic(provinO);
    	    		}
    	    		
    	    		
    	    		int sum1 = Integer.parseInt(str) + Integer.parseInt(statistic.get(provinI + type)); //ͳ���л��������ʡ��
    	    		int sum2 = Integer.parseInt(statistic.get(provinO + type)) - Integer.parseInt(str); //ͳ���л���������ʡ��
    	    		
    	    		statistic.put(provinI+ type , String.valueOf(sum1));
    	    		statistic.put(provinO + type , String.valueOf(sum2));
    			}
    			
    			else if(fileContent.get(i + 1).equals("����") || fileContent.get(i + 1).equals("����")) //�б�Ϊ�����������������
    			{
    				String provin = fileContent.get(i);  //��ȡ������ص�ʡ��
    	    		String type = fileContent.get(i + 1); //��ȡ�������˻���������
    	    		String str = fileContent.get(i + 2);
    	    		str = str.substring(0 , str.length() - 1); //��ȡ����
    	    		
    	    		if(!statistic.containsKey(provin + type))  //����ϣ�����Ƿ��Ѿ����ڸ�ʡ�ݵ�������
    	    		{
    	    			initStatistic(provin);
    	    		}
    	    				
    	    		int sum = Integer.parseInt(str) + Integer.parseInt(statistic.get(provin + type));			
    	    		int infect = Integer.parseInt(statistic.get(provin + "��Ⱦ����")) - Integer.parseInt(str);  //�����������˻��������ˣ���Ⱦ�����ִ�����Ҫ��ȥ
    	    		
    	    		statistic.put(provin + type , String.valueOf(sum));
    	    		statistic.put(provin + "��Ⱦ����" , String.valueOf(infect));
    			}
    			
    			else if(fileContent.get(i + 2).equals("ȷ���Ⱦ") || fileContent.get(i + 1).equals("�ų�")) //�б�Ϊȷ���Ⱦ���ų������
    			{
    				String provin = fileContent.get(i);  //��ȡ������ص�ʡ��
    	    		String str = fileContent.get(i + 3);
    	    		str = str.substring(0 , str.length() - 1); //��ȡ����
    	    		
    	    		if(!statistic.containsKey(provin + "���ƻ���"))  //����ϣ�����Ƿ��Ѿ����ڸ�ʡ�ݵ�������
    	    		{
    	    			initStatistic(provin);
    	    		}
    	    		
    	    		int infect = 0;
    	    		int unknownInfect = 0;
    	    		
    	    		if(fileContent.get(i + 2).equals("ȷ���Ⱦ")) //�����ȷ�ϸ�Ⱦ���Ǹ�Ⱦ��������Ҫ����
    	    		{
    	    			infect = Integer.parseInt(statistic.get(provin + "��Ⱦ����")) + Integer.parseInt(str);
    	    			statistic.put(provin + "��Ⱦ����" , String.valueOf(infect));
    	    		}
    	    		

    	    		unknownInfect = Integer.parseInt(statistic.get(provin + "���ƻ���")) - Integer.parseInt(str);  //�������ų�����ȷ����ƻ����ִ�����Ҫ��ȥ
    	    		
    	    		statistic.put(provin + "���ƻ���" , String.valueOf(unknownInfect));
    			}
    			
    		}
    	}
        
        public static void initStatistic(String provin)
    	/*
    	 * �÷������ڳ�ʼ��ͳ�����ݵĹ�ϣ�������־�г����˸�ʡ����Ϣ������г�ʼ��
    	 */
    	{
    		statistic.put(provin + "��Ⱦ����", "0");  //�����ϣ���в�û�д�Ÿ�ʡ�ݣ��ͳ�ʼ����ʡ��
    		statistic.put(provin + "���ƻ���", "0");
    		statistic.put(provin + "����", "0");
    		statistic.put(provin + "����", "0");
    	}		
        
        public static void countryStatic()
        /*
         *  �÷�������ͳ��ȫ������������
         */
    	{
    		int infect = 0;
    		int unknownInfect = 0;
    		int cure = 0;
    		int dead = 0;
    		for (String key : sortMap.keySet()) 
    		{
    			int num = Integer.parseInt(sortMap.get(key).toString());
    			if(key.substring(key.length()- 4, key.length()).equals("��Ⱦ����"))
    			{
    				infect += num;
    			}
    			else if(key.substring(key.length()- 4, key.length()).equals("���ƻ���"))
    			{
    				unknownInfect += num;
    			}
    			else if(key.substring(key.length()- 2, key.length()).equals("����"))
    			{
    				cure += num;
    			}
    			else if(key.substring(key.length()- 2, key.length()).equals("����"))
    			{
    				dead += num;
    			}			
    		}
    		
    		statistic.put("ȫ����Ⱦ����", String.valueOf(infect));
    		statistic.put("ȫ�����ƻ���", String.valueOf(unknownInfect));
    		statistic.put("ȫ������", String.valueOf(cure));
    		statistic.put("ȫ������", String.valueOf(dead));
    	}
        
    	
    	
    	
   

    
}
