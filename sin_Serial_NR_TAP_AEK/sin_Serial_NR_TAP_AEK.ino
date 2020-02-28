#include "Keyboard.h"
#include "Mouse.h"
#include "Math.h"
int buttonPin=11;
int PIN_LED = 13;
int incomingByte = 0;
int tate[] = {20, 13, 7, 7, 7,  7, 7, 8, 8, 8,   9, 9, 9, 9, 9,  9, 9, 10, 10, 10,  10, 10, 10, 10, 10,  10, 10, 10, 10, 10,  10};
int yoko[] = {0, -2, -1, -1, -1,   -1, -1, -1, -1, -2,   -2, -2, -2, -2, -2,   -2, -2, -2, -2, -2,  -2, -2, -2, -2, -2,  -2, -2, -2, -2, -2,   0};
int i = 0;
unsigned long time;
boolean flag=false;
int rpm= 900;
int rps = rpm/60;
int d = 1000/rps+1;
double b1= 0;
double b2= 0;
double tuyosa= 1;

void setup() {
  pinMode(buttonPin, INPUT_PULLUP);
  pinMode(PIN_LED, OUTPUT);
  digitalWrite(PIN_LED, LOW);
  Mouse.begin();
  Serial.begin(9600);
  delay(3000);
  Serial.print(d);
}

void loop() {
  i= 0;
  if(Serial.read() == 100){
      buppa(tuyosa);
  }else if (digitalRead(buttonPin)== LOW){
      tappu(tuyosa);
  }
}


void buppa(double t){
  while(true){
    digitalWrite(PIN_LED, HIGH);
    time = millis();
    //while (millis() - time < d/5) {}  //１発目が出る
    b1= (tate[i]*t);
    b1= round(b1);
    for(int c=0; c<b1; c++){
      Mouse.move(0, 1);
      //while (millis() - time <  ( (c+1)/(tate[i]+1) )*d ) {}
      delay(d/(b1+1));
    }
    b2= (yoko[i]*t);
    b2= round(b2);
    Mouse.move(b2, 0);
    while (millis() - time < d) {}
    if (i == (sizeof(tate)/sizeof(int)) - 1) {
      time = millis();
      while (millis() - time < 1000) {}
      break;
    }else if(Serial.read() == 111){
      if(flag){
        flag=false;
        break;
      }
      for(int c=0; c<b1; c++){
        Mouse.move(0, -1);
        //while (millis() - time <  ( (c+1)/(tate[i]+1) )*d ) {}
        delay(2);
      }
      Mouse.move(-b2, 0);
      break;
    }
    i++;
  }
  digitalWrite(PIN_LED, LOW);
}

void tappu(double t) {
  // デバウンス時間待機しても同じ状態ならスイッチ操作とみなす
  Mouse.press();
  time = millis();
  b1= (tate[0]*t);
  b1= round(b1);
  b2= (yoko[0]*t);
  b2= round(b2);
  while (millis() - time < d-10) {}  //１発目が出る
  Mouse.move(b2, b1);  //２発目に備えてリコイル制御
  while (millis() - time < d) {}
  
  time = millis();
  b1= (tate[1]*t);
  b1= round(b1);
  b2= (yoko[1]*t);
  b2= round(b2);
  while (millis() - time < d/2){}
  Mouse.move(yoko[1], tate[1]);   //３発目に備えてリコイル制御
  while (millis() - time < d) {}  //２発目が出る
  
  time = millis();
  while (millis() - time < d-40) {}  //２発目と同じ、３発目が出る
  Mouse.release();
  Mouse.move(0, 1);
  while (millis() - time < d) {}
  
  time = millis();
  while (millis() - time <= 105) {}
  flag=true;
  while(Serial.available()){ 
    Serial.read(); 
  } 
}
