CourseSchedule

# Background


# Environment
Android Studio 3.2.1
jdk 1.8
Target SDK version 28
Min SDK version 21
Virtual Device : Nexus 5X API 28 (1080*1920:420dpi)

# Features
Currently the following functions are implemented:(Get more details in description.docx)
    Login with HKU Portal UID and PIN.
    Query personal course schedule in the time table view.
    Add reminder to calendar.
    Caculate personal GPA.
    Check personal exam time.
    Add/Modify/Delete personal notes.


# Compile
This app's compileSdkVersion is API 28 while the minSdkVersion is API 21.
You can import the project and click 'Make Project' with Android Studio.
After downloading some required sdks, the project will build successfully.

# Execute
Create a virtual device and run 'app' on it. 
We recommend the hardware 'Nexus 5X' 1080 * 1920 because the app can get the best visual effect on this kind device.
Internet is necessary when using the app otherwise you can not login in.
Also, you'll be asked to grant permission about CALENDAR during use.

# Generate apk
If you want to install the app on your andorid devices, you need to generate the suitable apk.
Just click Build > Generate Signed APK > app > Create new (sign key) > Next ... > Finish > Reveal the apk in Finder.

# Dependencies
implementation project(':timetableview')
implementation 'com.google.code.gson:gson:2.8.2'
implementation 'pub.devrel:easypermissions:2.0.0'
implementation 'com.ashokvarma.android:bottom-navigation-bar:2.1.0'
implementation 'com.getbase:floatingactionbutton:1.10.1'
implementation 'com.baoyz.swipemenulistview:library:1.3.0'

And due to the fact that many of the dependences have been customized, we'd like to say thanks here to the original developers of these great libraries:
https://github.com/Ashok-Varma/BottomNavigation.git
https://github.com/yydcdut/PhotoNoter.git
https://github.com/Lemonreds/simplenote.git
https://github.com/federicoiosue/Omni-Notes.git
