CourseSchedule

# Background
Our MSc(CS) programme office has launched a homepage (https://www.msc-cs.hku.hk/) for several years to promote the programme and to attract potential students over the world. The site already has a mobile version but it is not tailor-made to the mobile environment. This is a smart phone application for our MSc(CS) programme.

# Main Function
You can get details in the description.docx.

# Compile
This app's compileSdkVersion is API 28 while the minSdkVersion is API 21.
You can import the project and click 'Make Project' with Android Studio.
After downloading some required sdks, the project will build successfully.

# Execute
Create a virtual device and run 'app' on it. 
We recommend the hardware 'Nexus 5X' 1080 * 1920 because the app can get the best visual effect on this kind device.
Internet is necessary when using the app otherwise you can not get the newest news & events.

# Generate apk
If you want to install the app on your andorid devices, you need to generate the suitable apk.
Just click Build > Generate Signed APK > app > Create new (sign key) > Next ... > Finish > Reveal the apk in Finder.

# Dependencies
They're all listed into the build.gradle file but due to the fact that many of the dependences have been customized by us. We would like to say thanks here to the original developers of these great libraries.
implementation project(':timetableview')
implementation 'com.google.code.gson:gson:2.8.2'
implementation 'pub.devrel:easypermissions:2.0.0'
implementation 'com.ashokvarma.android:bottom-navigation-bar:2.1.0'
implementation 'com.getbase:floatingactionbutton:1.10.1'
implementation 'com.baoyz.swipemenulistview:library:1.3.0'
