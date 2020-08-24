# GradlePractice

Gradleの設定の練習用リポジトリ。

ビルドタイプの設定。

    signingConfigs {
        release {
            storeFile file("key.jks")
            storePassword "123456"
            keyAlias "key0"
            keyPassword "123456"
        }
        staging {
            storeFile file("key.jks")
            storePassword "123456"
            keyAlias "key0"
            keyPassword "123456"
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
        staging {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    
フレーバーの設定
フレーバーとビルドタイプの組み合わせごとに別のアプリになります。
それぞれ別の機能を作ることが可能。ビルド後のapkに他のフレーバーのソースは入りません。

build.gradle(:app)

flavorDimensions "flavor"
    productFlavors {
        first_flavor_1 {
            dimension "flavor"
            //組み合わせごとにapplicationIdを設定し、組み合わせに応じて異なるアプリとしてパッケージ化
            applicationId "com.example.gradlepractice.firstflavor11"
            //AndroidManifest.xmlファイルのコンテンツをカスタマイズするプレースホルダー。次を使用：android：label = "$ {app_name}"
            manifestPlaceholders = [app_name: "first_flavor_1"]
        }
        first_flavor_2 {
            dimension "flavor"
            applicationId "com.example.gradlepractice.firstflavor12"
            manifestPlaceholders = [app_name: "first_flavor_2"]
        }
        second_flavor_1 {
            dimension "flavor"
            applicationId "com.example.gradlepractice.secondflavor21"
            manifestPlaceholders = [app_name: "second_flavor_1"]
        }
        second_flavor_2 {
            dimension "flavor"
            applicationId "com.example.gradlepractice.secondflavor22"
            manifestPlaceholders = [app_name: "second_flavor_2"]
        }
    }
}

dependencies {
    ・・・・・

    first_flavor_1Api project(path: ':FirstFlavor')
    first_flavor_2Api project(path: ':FirstFlavor')
    second_flavor_1Api project(path: ':SecondFlavor')
    second_flavor_2Api project(path: ':SecondFlavor')
}

build.gradle(:FirstFlavor)
appのgradleで定義されたものの中からFirstFlavor用のものだけ定義。

buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        staging {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    //フレーバーはカスタムディメンションであり、同じアプリに設定されている場合、チャネルを切り替えると、libは自動的にスイッチに従う
    flavorDimensions "flavor"
    publishNonDefault true
    productFlavors {
        first_flavor_1 {
            dimension "flavor"
            //buildConfig差分割り当て
            buildConfigField("String", "from", "\"first_flavor_1\"")
        }
        first_flavor_2 {
            dimension "flavor"
            buildConfigField("String", "from", "\"first_flavor_2\"")
        }
    }

build.gradle(:SecondFlavor)
appのgradleで定義されたものの中からSecondFlavor用のものだけ定義。

buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        staging {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    flavorDimensions "flavor"
    publishNonDefault true
    productFlavors {
        second_flavor_1 {
            dimension "flavor"
            //buildConfig差分割り当て
            buildConfigField("String", "from", "\"second_flavor_1\"")
        }
        second_flavor_2 {
            dimension "flavor"
            buildConfigField("String", "from", "\"second_flavor_2\"")
        }
    }
    
これでBuildVariantにBuildTypeとFlavorの組み合わせでアプリが選択できます。FirstFlavor関連を選んだ場合、SecondFlavorはFirstFlavorを持っていないためビルドに含まれません。
![GradlePractice  CUsersuserAndroidStudioProjectsGradlePractice  -  appsrcfirst_flavor_2reslayoutactivity_main xml  GradlePractice app  - Android Studio 20200816 234733](https://user-images.githubusercontent.com/37768294/91020168-1779da80-e62d-11ea-8ff9-73598f2ea11f.png)

今回はMainActivityもFlavorごとに作成。（GradlePractice\app\src\main以外フォルダを作らなければmain配下のMainActivityに処理を共通化することも可能）
GradlePractice\app\src配下にfirst_flavor_1、first_flavor_2、second_flavor_1、second_flavor_2フォルダを作り、それぞれの配下にソースファイルを配置します。
AndroidManifestのlabelについては下記のように指定することで、appのgradleで指定したmanifestPlaceholders = [app_name: "XXX"]が適用されます。

    <application
        ・・・
        android:label="${app_name}"
        ・・・
    </application>
    
それぞれのフレーバーでの実行結果。アプリのタイトルとフレーバーを示すテキストがそれぞれ別のものになっていることがわかります。
また、今回、レイアウトファイルもそれぞれ別のものにしたためテキストの配置位置がそれぞれ異なるものを作成することができました。

![Android Emulator - Nexus_5X_API_275556 20200816 234458](https://user-images.githubusercontent.com/37768294/91021005-2614c180-e62e-11ea-80b4-1979bca1676d.png)
![Android Emulator - Nexus_5X_API_275556 20200816 234353](https://user-images.githubusercontent.com/37768294/91021001-257c2b00-e62e-11ea-9af5-351f687511e3.png)
![Android Emulator - Nexus_5X_API_275556 20200816 235027](https://user-images.githubusercontent.com/37768294/91021007-2614c180-e62e-11ea-9989-87548b3c7971.png)
![Android Emulator - Nexus_5X_API_275556 20200816 235227](https://user-images.githubusercontent.com/37768294/91020996-244afe00-e62e-11ea-8ec2-ea4a8daa6aa3.png)

Ext:
gradleファイルが複数になってしまったためおまじないのように使う設定に関しては一つにまとめたいと思います。
Java1.8を使う場合、下記の記述を行う必要があります。（Java1.8はコードの記述を楽にしてくれる-ktx系やWorkManagerなどで必要なので出番が多いです）

android {
      ...
      // Configure only for each module that uses Java 8
      // language features (either in its source code or
      // through dependencies).
      compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
      }
      // For Kotlin projects
      kotlinOptions {
        jvmTarget = "1.8"
      }
    }
    
モジュールを作るたびにこの記述をするのは少々面倒くさいと思います。忘れた場合にエラーを確認するのも少々面倒くさいです。
なので一つにまとめます。下記の記述でまとめることに成功しました。

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).all {
        kotlinOptions { jvmTarget = "1.8" }
    }
    subprojects {
        afterEvaluate { project ->
            if (project.hasProperty("android")) {
                android {
                    compileOptions {
                        sourceCompatibility JavaVersion.VERSION_1_8
                        targetCompatibility JavaVersion.VERSION_1_8
                    }
                }
            }

        }
    }
