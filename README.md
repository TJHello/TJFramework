# TJFramework [![](https://jitpack.io/v/TJHello/TJFramework.svg)](https://jitpack.io/#TJHello/TJFramework)
一款自己用的android快速开发框架，在不断的完善中
- - -
- ## **框架引用**

  * **Step 1. 添加Jitpack仓库到你的项目build.gradle**
  ```groovy
    allprojects {
	    repositories {
		    ...
		    maven { url 'https://jitpack.io' }
	    }
    }
  ```
  * **Step 2. 添加远程库到app-build.gradle**
  ```groovy
    dependencies {
        api 'com.github.TJHello:TJFramework:***'
    }
  ```

  * **Step 3. app-build.gradle配置支持JAVA8**

  ```groovy
    android {
        ...
        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }
    }
  ``` 


- ## **框架配置**

  #### **1. Application配置**

  ```java
    //在AndroidManifest-Application中增加
    android:name="com.tjbaobao.framework.base.BaseApplication"
    //如果已经有了别的Application基类，请在Application-onCreate中增加以下代码
    com.tjbaobao.framework.base.BaseApplication.init(this);
  ```
    
  #### **2. 数据库配置**

  ```
    //在AndroidManifest-Application中增加
    <meta-data android:name="database_name" android:value="TjFramework" />
    <meta-data android:name="database_version" android:value="1"/>
  ```

    
- ## **框架入门**

  + #### **activity、fragment、adapter等基类的使用(直接继承即可，具体API请看具体类)**
  <pre>
  <code>
     - BaseActivity
     - BaseAdapter
     - BaseApplication
     - BaseFragment
     - BaseFragmentActivity
     - BaseItemDecoration
     - BaseRecyclerAdapter
     - BaseV4Fragment
     <p>以下是二次封装基类,规定了一定的结构，以及便捷标题栏的使用。</p>
     - TJActivity
     - TJFragment
     - TJFragmentActivity
  </code>
  </pre>
   + #### **database基类BaseDataBaseHelper的使用(直接继承即可，具体API请看具体类)**
 
   <pre>
   <code>
    - BaseDataBaseHelper
   </code>
   </pre>

   + #### **Dialog基类的使用(直接继承即可，具体API请看具体类)**

   <pre>
   <code>
    -- BaseDialog//弹窗封装，带有动画等
   </code>
   </pre>

   + #### **view基类的使用**

  <pre>
  <code>
    - BaseUI //UI基类
    - BaseLinearLayout
    - BaseRelativeLayout
    - BaseRecyclerView //RecyclerView封装
    - BaseTitleBar//多功能标题栏
    - ClickTabbar//点击选择Tabbar
  </code>
  </pre>

   + #### **工具类的使用**

   <pre>
   <code>
    -- BaseHandler//handler封装，Created by Dmytro Voronkevych
    -- BaseTimerTask//TimerTask封装
    -- ConstantUtil//常量类，主要封装了地址获取方法
    -- DateTimeUtil//日期时间处理类
    -- DeviceUtil//设备信息获取类
    -- Equation//封装了一些求两点距离，矩形相交等关于数学的方法
    -- ExecuteLog//log处理类
    -- FileDownloader//文件下载类，对OKHttp的封装，支持磁盘缓存，优先下载队列，下载进度等等。
    -- FileUtil//文件处理类
    -- FontManager//字体管理器
    -- HexConvertTools//字节转换工具
    -- ImageDownloader//图片加载器，支持异步处理，内存、磁盘缓存，优先下载队列，图片剪裁压缩等等
    -- NetworkUtil//网络处理类(已经弃用)
    -- OKHttpUtil//OKHttp的封装类
    -- ResourcesGetTools//资源获取器,用于获取图库图片，视频，拍照，录像，以及磁盘文件等
    -- SVGUtil//SVG文件处理工具
    -- Tools //常用工具集合,日志输出，Toast等等
    -- ValueTools//字节于对象之间的转换
   </code>
   </pre>
