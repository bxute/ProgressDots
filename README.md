## ProgressDotView [![](https://jitpack.io/v/bxute/ProgressDots.svg)](https://jitpack.io/#bxute/ProgressDots)

An android view which shows the current position of progress with a nicely animated dots and transitioning colors.

<img src="https://user-images.githubusercontent.com/10809719/41149642-bcc1df50-6b29-11e8-9e76-fc17f3e926d3.gif" width="360px" height="640px">

**Use Case:** It can be used:
 - On onboarding pages for showing current position
 - On Sliding banners
 - On Any page which has fixed set of steps

### How to use this library
---
**For Gradle**

 1. Add the JitPack repository to your build file(project level)
 ```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add the dependency
```
dependencies {
	        implementation 'com.github.bxute:ProgressDots:v1.0'
	}
```

---

**For Maven**
1. Add the JitPack repository to your build file(project level)
```
<repositories>
	<repository>
		<id>jitpack.io</id>
		 <url>https://jitpack.io</url>
	</repository>
</repositories>
 ```
 
 2. Add the dependency
 ```
<dependency>
	 <groupId>com.github.bxute</groupId>
	 <artifactId>ProgressDots</artifactId>
	 <version>v1.0</version>
</dependency>
```

---

**For sbt**
1. Add the JitPack repository to your build file(project level)
```
 resolvers += "jitpack" at "https://jitpack.io"     
```

2. Add the dependency
```
libraryDependencies += "com.github.bxute" % "ProgressDots" % "v1.0"	
```

Now add to your layout file

```xml
 <xute.progressdot.ProgressDotView xmlns:app="http://schemas.android.com/apk/res/xute.progressdotsSample"
        android:id="@+id/dotProgressView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        app:activeDotColor="#095199"
        app:dotCount="6"
        app:inActiveDotColor="#b9b9b9"
        app:largeDotRadius="6dp"
        app:smallDotRadius="4dp"
        app:spaceBetweenDots="4dp"
        app:switchTimeInMillis="1000" />
```

Then you need to call `moveToNext()` method to move the active dot 1 step forward.
Like:
```java
progressDotView = findViewById(R.id.dotProgressView);
progressDotView.moveToNext();
```

### Customizing View
You can customize this view using a set of attributes and member methods.

**Change Active Dot color**

Using XML
```xml
 app:activeDotColor="#095199"
```
Using JAVA
```java
progressDotView.setActiveDotColor(Color.parseColor("#009988"));
```

Similarly you can call other methods:
`setDotCount(int)` `setActiveDotColor(int)` `setInactiveDotColor(int)` `setSmallDotRadiusInDp(int)` etc.


### Contributions

Any contributions are welcome. You can send PR or open issues.

### License
MIT License

Copyright (c) 2018 bxute

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
