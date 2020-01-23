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
  delay(3000);
  Serial.print(d);
}

void loop() {
  i=0;
  if(Serial.read() == 100){
      buppa();
  }else if (digitalRead(buttonPin)== LOW){
      tappu();
  }
}


void buppa(){
  while(true){
    time = millis();
    while (millis() - time < d/2) {}  //１発目が出る
    Mouse.move(yoko[i], tate[i]);
    while (millis() - time < d) {}
    flag=false;
    if (i == (sizeof(tate)/sizeof(int)) - 1) {
      time = millis();
      while (millis() - time < 1000) {}
      break;
    }else if(Serial.read() == 111&&!flag){
      Mouse.move(-yoko[i], -tate[i]);
      break;
    }
    i++;
  }
}

void tappu() {
  // デバウンス時間待機しても同じ状態ならスイッチ操作とみなす
  Mouse.press();
  time = millis();
  while (millis() - time < 50) {}  //１発目が出る
  Mouse.move(yoko[0], tate[0]);  //２発目に備えてリコイル制御
  while (millis() - time < d) {}
  
  time = millis();
  while (millis() - time < d/2){}
  Mouse.move(yoko[1], tate[1]);   //３発目に備えてリコイル制御
  while (millis() - time < d) {}  //２発目が出る
  
  time = millis();
  while (millis() - time < d-50) {}  //２発目と同じ、３発目が出る
  Mouse.release();
  while (millis() - time < d) {}
  
  time = millis();
  while (millis() - time <= 50) {}
  flag=true;
}
