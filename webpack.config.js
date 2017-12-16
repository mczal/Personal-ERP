const path = require('path');
const webpack = require('webpack');

module.exports = {

  entry: {
    application: './webpack/javascripts/application.js'
  },

  output: {
    filename: 'javascripts/[name].js',
    path: path.resolve(__dirname, 'src/main/resources/static')
  },

  watchOptions: {
    poll: 1000,
    ignored: /node_module/
  },

  module: {
    loaders: [
    {
      test: /\.js$/,
      loader: 'babel-loader',
      exclude: /node_modules/,
      query: {
        presets: ['es2015']
      }
    }
    ],
  },

  stats: {
    colors: true
  }
};
