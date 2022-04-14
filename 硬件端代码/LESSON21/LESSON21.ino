/***********************************
  购买地址：ILoveMCU.taobao.com
  修正代码：神秘藏宝室
***********************************/
/*--------Arduino Uno connection to LCD-----------------------------------------------

          LCD Module                  Arduino Uno
      
      GND-------------------------GND
       VDD-------------------------5.0V
       RST(Floating)
       SCL-------------------------A5(PC5)
       SDA-------------------------A4(PC4)
       BUSY------------------------A3(PC3)
       A1(Floating)
       A0(Floating)
       
-------------------------------------------------------------------------------------*/
#include <SoftwareSerial.h>
#include <RSCG12864B.h>
#include <Servo.h>

Servo myservo;  // 定义Servo对象来控制
int pos = 0;    // 角度存储变量
char chn[]={0XB4,0XF3,0XD1,0XA7,0XC9,0XFA,0XB5,0XE7,0XD7,0XD3,0XC9,0XCC,0XC6,0XCC,0};			//最后加0，为了让显示函数能判定何时结束

char park[]={0XCD,0XA3,0XB3,0XB5,0XB3,0XA1,0XB9,0XDC,0XC0,0XED,0XCF,0XB5,0XCD,0XB3,0};

char tt[]={0XB9,0XB2,0XD3,0XD0,0XB3,0XB5,0XCE,0XBB,0XA3,0XBA,0};//共有车位
char tt1[]={0XB9,0XB2,0XD3,0XD0,0XB3,0XB5,0XCE,0XBB,0XA3,0XBA,0};
char tt2[]={0XCE,0XBB,0};//位
char ll[]={0XCA,0XA3,0XD3,0XE0,0XB3,0XB5,0XCE,0XBB,0X3A,0};

char f1[]={0X49,0X32,0X43,0XBD,0XD3,0XBF,0XDA,0X2C,0XC4,0XDA,0XBD,0XA8,0XD7,0XD6,0XBF,0XE2,0};
char f2[]={0XD6,0XD0,0XCE,0XC4,0X2C,0XBA,0XAB,0XCE,0XC4,0X2C,0XC8,0XD5,0XCE,0XC4,0,};
char f3[]={0XD6,0XA7,0XB3,0XD6,0X32,0X44,0XBB,0XE6,0XCD,0XBC,0};

String rec;

SoftwareSerial ESP_Serial(6, 7); 
void setup() {
  // put your setup code here, to run once:
  myservo.attach(9); 
  Serial.begin(115200);
  ESP_Serial.begin(9600);
  RSCG12864B.begin();
  RSCG12864B.brightness(10);
}

void loop() {
  // put your main code here, to run repeatedly:

  // RSCG12864B.display_bmp(0);
  // delay(800);
  // RSCG12864B.display_bmp(1);
  // delay(800);
  // RSCG12864B.display_bmp(2);
  // delay(800);
  // RSCG12864B.display_bmp(3);
  // delay(800);
  // RSCG12864B.display_bmp(4);
  // delay(800);
  // RSCG12864B.display_bmp(5);    
  // delay(800);
   while (ESP_Serial.available() > 0)
  {
    rec = ESP_Serial.readString();
   // Serial.println(rec);
   String aa = rec.substring(0,2);
   String b = rec.substring(2,4);
   String c = rec.substring(4,20);
   
   String d = rec.substring(23,25);
   //Serial.println(rec);
    Serial.println(aa);
    Serial.println(b);
    Serial.println(c);
    Serial.println(d);
  if(d=="aa"){
   for (pos = 90; pos >= 0; pos --) { // 从180°到0°
    myservo.write(pos);              // 舵机角度写入
    delay(5);                       // 等待转动到指定角度
  }
  delay(5000);
  for (pos = 0; pos <= 90; pos ++) { // 0°到180°
    // in steps of 1 degree
    myservo.write(pos);              // 舵机角度写入
    delay(5);                       // 等待转动到指定角度
  }
 }
   
    char buf[aa.length() + 1];
  aa.toCharArray(buf, aa.length() + 1);
  RSCG12864B.clear();

  char buf1[b.length() + 1];
  b.toCharArray(buf1, b.length() + 1);

  char buf2[c.length() + 1];
  c.toCharArray(buf2, c.length() + 1);
  
  RSCG12864B.print_string_16_xy(0,0,park);
   RSCG12864B.print_string_16_xy(0,16,tt);

  RSCG12864B.print_string_16_xy(80,16,buf);
  RSCG12864B.print_string_16_xy(95,16,tt2);
  RSCG12864B.print_string_16_xy(0,32,ll);//剩余车位
   RSCG12864B.print_string_16_xy(80,32,buf1);//剩余数量
  RSCG12864B.print_string_16_xy(95,32,tt2);//位

  RSCG12864B.print_string_16_xy(0,48,buf2);//剩余车位
  delay(3000);
    
  }

 // RSCG12864B.clear();
 // RSCG12864B.print_string_12_xy(0,15,chn);
  //RSCG12864B.print_string_12_xy(8,35,"ilovemcu.taobao.com");
  
 // delay(3000);
 // RSCG12864B.clear();
  //RSCG12864B.font_revers_on();			//反白操作
  //RSCG12864B.print_string_12_xy(25,0,"Built-in font");
  //RSCG12864B.font_revers_off();			//关闭反白
  
 // RSCG12864B.print_string_5x7_xy(3,15,"5*7 ASCII 0123456789");
  //RSCG12864B.print_string_5x7_xy(3,25,"5*7 ASCII ABCDEabcde");
  //RSCG12864B.print_string_12_xy(20,35,"6*12/12*12 FONT");
  //RSCG12864B.print_string_16_xy(5,48,"8*16/16*16 FONT");
  //delay(3000);

  //RSCG12864B.clear();
  //RSCG12864B.print_string_16_xy(0,0,f1);
  //RSCG12864B.print_string_16_xy(0,16,f2);
  //RSCG12864B.print_string_16_xy(0,32,f3);
 // delay(1000);
 // RSCG12864B.draw_rectangle(0,50,127,63);
 // while(1);
 // for(int i=2;i<=125;i++)
 // {
 //   RSCG12864B.draw_fill_rectangle(2,52,i,61);
  //  delay(100);
 // }
 // delay(3000);
  
 // char text[50];
//  RSCG12864B.clear();
  //for(int i = 0 ; i < 300 ; i++)
  //{
   // sprintf(text,"ilovemcu.taobao.com %d",i);    
   // RSCG12864B.print_string_12_xy(0,20,text);
   // delay(1000);
  //}
  
}
