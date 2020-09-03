const path = require("path");
const fs = require("fs");

const SOURCE_DIR = "src";

const JS_REGEX = /\.js$/;

const entries = {};
for (const p of fs.readdirSync(SOURCE_DIR)) {
  const fullpath = path.resolve(__dirname, SOURCE_DIR, p);
  if (!fs.statSync(fullpath).isDirectory()) {
    const { name, ext } = path.parse(p);
    if (!JS_REGEX.test(ext)) continue;
    entries[name] = fullpath;
  }
}

module.exports = {
  entry: entries,
  output: {
    path: path.resolve(__dirname, "../src/main/webapp/build/js"),
    filename: "[name].js",
    publicPath: "http://localhost:9000/",
  },
  module: {
    rules: [
      {
        test: JS_REGEX,
        exclude: /(node_modules|bower_components)/,
        use: {
          loader: "babel-loader",
          options: {
            presets: [
              "@babel/preset-env",
              "@babel/react",
              {
                plugins: ["@babel/plugin-proposal-class-properties"],
              },
            ],
          },
        },
      },
      {
        test: /\.css$/i,
        use: ["style-loader", "css-loader"],
      },
      {
        test: /\.(webm|mp4)$/i,
        use: 'file-loader?name=videos/[name].[ext]',
      },
      {
        test: /\.(png|jpg|gif|svg)$/i,
        use: [
          {
            loader: "url-loader",
            options: {
              limit: 8192,
            },
          },
        ],
      },
    ],
  },
  devServer: {
    port: 9000,
    headers: {
      "Access-Control-Allow-Origin": "http://localhost:8080",
      "Access-Control-Allow-Headers": "*",
    },
  },
};
