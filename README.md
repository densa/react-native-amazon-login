
# react-native-amazon-login

## Getting started

`$ npm install react-native-amazon-login --save`

### Mostly automatic installation

`$ react-native link react-native-amazon-login`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-amazon-login` and add `RNAmazonLogin.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNAmazonLogin.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNAmazonLoginPackage;` to the imports at the top of the file
  - Add `new RNAmazonLoginPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-amazon-login'
  	project(':react-native-amazon-login').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-amazon-login/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-amazon-login')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNAmazonLogin.sln` in `node_modules/react-native-amazon-login/windows/RNAmazonLogin.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Amazon.Login.RNAmazonLogin;` to the usings at the top of the file
  - Add `new RNAmazonLoginPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNAmazonLogin from 'react-native-amazon-login';

// TODO: What to do with the module?
RNAmazonLogin;
```
  