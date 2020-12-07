package com.example.test1218;

import android.util.Log;

public class DataMode {

	static int[] buffer01 = new int[6]; //new int[6];
	private static int[] buffer_mode = {0, 0, 0, 0};
	private static final int minus = -1;
	static boolean ValueFlag01 = false;
	
	 // 통신 모드 변경(2015-01-19)
	public static int MODE = 0;
    public static final int M_PKST = 1; // data_accelerate
    public static final int M_PKLT = 2; // data_magnetic
    public static final int M_DEFAULT = 0;
    
    public static short test01, test02, test03, test04, test05, test06;
    private static int mode_index=0;
	
    DataMode(){
    	
    }

      static void m_pkst(int return_msg){
    	  
    	  buffer01[mode_index]=return_msg;
			
			switch(mode_index){
			case 1: test01 = calculData(1); break;
			case 3: test02 = calculData(3); break;
			case 5: test03 = calculData(5); initial_mode(); 
					ValueFlag01 = true; 
					break;
			}
		
		mode_index++;	
		
      }
      
      static void m_pkbma(int return_msg){
    	  
    	  buffer01[mode_index]=return_msg;
			
			switch(mode_index){
			case 1: test01 = calculData2(1); break;
			case 3: test02 = calculData2(3); break;
			case 5: test03 = calculData2(5); initial_mode(); 
					ValueFlag01 = true; 
					break;
			}
		
		mode_index++;	
		
      }
      
      static void m_pklt(int return_msg){
    	  
    	  buffer01[mode_index]=return_msg;
			
			switch(mode_index){
			case 1: test04 = calculData(1); break;
			case 3: test05 = calculData(3); break;
			case 5: test06 = calculData(5); initial_mode(); 
//					ValueFlag01 = true; 
					break;
			}
			
		mode_index++;	
		
      }
      
      static void initial_mode(){
    	  
    	mode_index=minus;
		
		for(int ch=0;ch<buffer_mode.length;ch++)
			buffer_mode[ch] = 0;
		for(int ch=0;ch<buffer01.length;ch++)
			buffer01[ch] = 0;
		
		MODE = M_DEFAULT;	
      }
      
      static void m_default(int return_msg){
    	    	  
    	  switch(return_msg){
			
			case 80 :  buffer_mode[0]=return_msg; break; //'P'
			case 75 :  buffer_mode[1]=return_msg; break; //'K'
			case 83 :  buffer_mode[2]=return_msg; break; //'S'
			case 76 :  buffer_mode[2]=return_msg; break; //'L'
			case 84 :  buffer_mode[3]=return_msg; break; //'T'
			
			}
    	  
    	 // PKST={80,75,83,84} PKLT={80,75,76,84}
    	  
		if(buffer_mode[0]==80 && buffer_mode[1]== 75 && buffer_mode[2]==83 && buffer_mode[3]==84)
			MODE = M_PKST;
		else if(buffer_mode[0]==80 && buffer_mode[1]== 75 && buffer_mode[2]==76 && buffer_mode[3]==84)
			MODE = M_PKLT;

      }
      
	  static short calculData(int i){
		  
		  short t;
		  
			if(buffer01[i]>=0){		
				
				t =  (short)((buffer01[i])<<8);
				buffer01[i-1] &= 0x00ff;
				t= (short) (t+(short)(buffer01[i-1]));
				
			}else{	
				
				t =  (short)(buffer01[i]);
				t &= 0xff00;
				t = (short)(buffer01[i-1]); 
			}		
			return t;
		}
      
	  static short calculData2(int i){
		  
		  short t;
		  
			if(buffer01[i]>=0){		
				
				t =  (short)((buffer01[i])<<8);
				buffer01[i-1] &= 0x00ff;
				t= (short) (t+(short)(buffer01[i-1]));
				
			}else{	
				
				t =  (short)(buffer01[i]);
				t &= 0xff00;
				t = (short)(buffer01[i-1]); 
			}		
			return (short)(t>>6);
		}
}
