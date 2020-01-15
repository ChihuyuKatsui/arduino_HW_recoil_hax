#include "Keyboard.h"
#include "Mouse.h"
#include "Math.h"

int buttonPin=11;
int PIN_LED = 13;
int incomingByte = 0;
int tate[] = {15, 12, 7, 7, 7,   7, 7, 7, 7, 7,  7, 7, 7, 7, 7,  7, 7, 7, 7, 7,   7, 7, 7, 7, 7,   7, 8, 8, 8, 8, 8};
int yoko[] = {0, -1, 0, 0, 0,  0, 0, -1, -1, -2,  -2, -2, -2, -2, -2,   -2, -2, -2, -2, -2,   -2, -2, -2, -2, -2,   -2, -2, -2, -2, -2, 0};
int i = 0;
unsigned long time;
boolean flag=false;
int rpm= 770;
int rps = rpm/60;
int d = 1000/rps+1;


void setup() {
  pinMode(buttonPin, INPUT_PULLUP);
  pinMode(PIN_LED, OUTPUT);
  digitalWrite(PIN_LED, LOW);
  Mouse.begin();
  Serial.begin(9600);
  Serial.print(d);
}

void loop() {
    if (digitalRead(buttonPin)== LOW){
      tappu();
    }else{
      buppa();
    }
}


void buppa(){
  i = 0;
  if(Serial.read() == 100&&!flag){
    while (true) {
      time = millis();
      while (millis() - time < d/2) {}  //１発目が出る
      Mouse.move(yoko[i], tate[i]);
      while (millis() - time < d) {}
      i++;
      if (i == (sizeof(tate)/sizeof(int)) - 1) {
        time = millis();
        while (millis() - time < 1000) {}
        break;
      } else if(Serial.read() == 111){
        Mouse.move(-yoko[i], -tate[i]);
        break;
      }
    }
  }
  flag=false;
}

void tappu() {
  // デバウンス時間待機しても同じ状態ならスイッチ操作とみなす
  Mouse.press();
  time = millis();
  while (millis() - time < 45) {}  //１発目が出る
  Mouse.move(0,15);  //２発目に備えてリコイル制御
  while (millis() - time < d) {}
  
  time = millis();
  while (millis() - time < d/2){}
  Mouse.move(-1,12);   //３発目に備えてリコイル制御
  while (millis() - time < d) {}  //２発目が出る
  
  time = millis();
  while (millis() - time < d-45) {}  //２発目と同じ、３発目が出る
  Mouse.release();
  while (millis() - time < d) {}
  
  time = millis();
  while (millis() - time <= 50) {}
  flag=true;
}
