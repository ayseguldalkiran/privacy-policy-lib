# Privacy Policy

Gizlilik Politikası paketi uygulamanın Gizlilik Politikası Sözleşmesini kullanıcıya gösterme ve onaylama işlemlerini SOAP servislerini kullanarak yönetmeyi sağlar.

## Gereksinimler

1. local.properties dosyasında nexusUsername ve nexusPassword base64 formatında encoded olarak tanımlanmalıdır.
2. Nexus sunucusunun SSL sertifikası alınmalıdır. Tarayıcı üzerinden veya terminalden indirilebilir.
3. Java'nın cacerts truststore'una sertifika eklenmelidir. Aşağıdaki komut ile eklenebilir:
```sh
sudo keytool -importcert -alias nexus-cert -file /path/to/nexus.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit
```
Burada:
- /path/to/nexus.crt → Sertifika dosya yolu
- $JAVA_HOME → Java kurulu dizin
- changeit → Java truststore'un default şifresi.

#### 🔧 Geçici Çözüm (Sertifika Doğrulaması Hataları İçin)

> **Not:** Eğer sertifika doğrulama nedeniyle işlem gerçekleştirilemiyorsa ve yapıların acil olarak yayınlanması gerekiyorsa, aşağıdaki yöntemler _geçici_ olarak kullanılabilir.

gradle.properties dosyasına aşağıdaki satırı eklemek:
```sh
systemProp.gradle.insecure=true
```
ya da terminalden şu parametre ile çalıştırmak:
```sh
./gradlew publishMavenPublicationToNexusRepository -Djavax.net.ssl.trustStore=/path/to/custom/truststore
```

**Ancak bu yöntemler güvenlik açısından önerilmez !!!**

## Kullanım
- Her versiyon yayınlama öncesinde build.gradle dosyasında publishing içerisinde version numarası arttırılmalıdır.
- Daha sonra terminalde
```sh 
./gradlew publishMavenPublicationToNexusRepository
```
komutu çalıştırılmalıdır.
## Kullanılacak projede yapılacaklar:
- build.gradle(module) dosyasında dependencies içerisine
```sh
implementation "com.logo.androidlibs:privacy-policy-lib:version"
```
şeklinde eklenmelidir.
-  build.gradle(project) dosyasında repositories bloğu üzerinde
```sh
    def props = new Properties()
    props.load(new FileInputStream(rootProject.file("local.properties")))
    def nexusUsername = props.getProperty("nexusUsername") ? new String(Base64.decoder.decode(props.getProperty("nexusUsername"))) : ""
    def nexusPassword = props.getProperty("nexusPassword") ? new String(Base64.decoder.decode(props.getProperty("nexusPassword"))) : ""
````
şeklinde eklenmelidir.
-  build.gradle(project) dosyasında repositories bloğu içerisine
```sh
    maven {
            url "https://sonarnexus.logo.com.tr:8443/repository/android-packages/"
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
````
şeklinde eklenmelidir.