#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClientSecureBearSSL.h>

WiFiClient client;

// WiFi
const char* ssid = "my_ssid";
const char* password =  "my_password";

// Server
const String apiUrl = "https://my-domain";
String mac; 
const int sensorGpio = A0;
float sensorAvg = 0;

void setup() {
  Serial.begin(9600);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  if (WiFi.waitForConnectResult() != WL_CONNECTED) {
    // Serial.println("Wifi connection attempt failed");
    return;
  }
  // Serial.println("WiFi connected");

  mac = WiFi.macAddress();
}


void loop() {
  const int sensorAtZeroMoisture = 909;
  const int sensorInWater = 580;
  
  float sensorValue = analogRead(A0);
  Serial.println(sensorValue);
  int moisture = (sensorAtZeroMoisture - sensorValue) * 100 / (sensorAtZeroMoisture - sensorInWater);

  // WiFiClientSecure client;
  std::unique_ptr<BearSSL::WiFiClientSecure>client(new BearSSL::WiFiClientSecure);
  client->setInsecure();
  HTTPClient https;
  if (https.begin(*client, "https://garden.jennundmicha.de/moisture/")) {
    https.addHeader("Content-Type", "application/json");
    String json = "{ \"mac\":\"" + mac + "\", \"value\":" + moisture + "}";
    int httpCode = https.POST(json); // send request

    /* if (httpCode < 0) {
      Serial.printf("[HTTPS] POST... code: %d\n", httpCode);
    } else {
       Serial.printf("[HTTPS] POST... failed, error: %s\n", https.errorToString(httpCode).c_str());
    }
     Serial.println("end");
     */
    https.end(); // close connection
  } else {
    // Serial.println("[HTTPS] unable to connect");
  }
  ESP.deepSleep(895e6); //14:55 min
}
