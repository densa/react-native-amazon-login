/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import AmazonLogin from 'react-native-amazon-login'

type Props = {};
export default class App extends Component<Props> {
  state = {
    result: null,
  }

  onLogin = async () => {
    try {
      const result = await AmazonLogin.login()
      this.setState({ result })
    } catch (error) {
      console.log('-----------')
      console.log(error)
      console.log('-----------')
    }
  }

  onLogout = async () => {
    try {
      await AmazonLogin.logout()
      this.setState({ result: null })
    } catch (error) {
      console.log('-----------')
      console.log(error)
      console.log('-----------')
    }
  }

  render() {
    return (
      <View style={styles.container}>
        {Boolean(this.state.result) ?
          <View>
            <Text>{this.state.result.token}</Text>
            <TouchableOpacity onPress={this.onLogout}>
              <View style={styles.button}><Text style={styles.buttonText}>Logout</Text></View>
            </TouchableOpacity>
          </View> :
          <TouchableOpacity onPress={this.onLogin}>
            <View style={styles.button}><Text style={styles.buttonText}>Login</Text></View>
          </TouchableOpacity>
        }
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  button: {
    width: 150,
    borderColor: 'black',
    borderWidth: 1,
  },
  buttonText: {
    marginVertical: 5,
    textAlign: 'center',
  }
});
