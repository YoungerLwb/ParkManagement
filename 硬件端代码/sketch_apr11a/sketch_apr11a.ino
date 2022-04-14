

#include <ESP8266WiFi.h>       
const char* ssid     = "liuwenbo";      
                                           
const char* password = "liu980627";          
const char* host = "192.168.43.150";                                          
WiFiClient client;                                            
void setup() {
  Serial.begin(9600);         // 启动串口通讯
  
  WiFi.begin(ssid, password);                  
  //Serial.print("Connecting to ");              
  //Serial.print(ssid); Serial.println(" ...");  
  
  int i = 0;                                   
  while (WiFi.status() != WL_CONNECTED) {      
    delay(1000);                                                  
   // Serial.print(i++); Serial.print(' ');      
  }                                            
                                               
  //Serial.println("");                         
  //Serial.println("Connection established!");   
 // Serial.print("IP address:    ");             
 // Serial.println(WiFi.localIP());              

void loop() {    
   //Serial.println(WiFi.localIP());   
  if (client.connect(host, 60020))
  {
    //Serial.print("Connected to: ");
   // Serial.println(host);

    /* Send "connected" to the server so it knows we are ready for data */
    client.println("deviceconnected"); //USE client.println()!!
    //Serial.println("Host message: \n");

    /* Wait for data for 5 seconds at most before timing out */
    unsigned long timeout = millis();
   while(client.available() == 0)
    {
      if(millis() - timeout > 1000)
      {
        //Serial.println("Timeout to server!");
        break;
      }
    }

    /* Read in the data in the stream */
    while(client.available() > 0)
    {
      String rec = client.readStringUntil('\n');
      //String a = rec.substring(0,2);
     // String b = rec.substring(2,3);
     // String c = rec.substring(3,19);
     // Serial.println(a);
     // Serial.println(b);
     // Serial.println(c);
      Serial.print(rec);
    }
    client.stop();
  }
  else
  {
    client.stop();
  }
  delay(1000);                               
}
