const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const path = require('path')
const fs = require('fs')
const entryMap = {}

fs.readdirSync('./src/main/js/')
    .filter(file => file.match(/.*\.js$/))
    .forEach(f => {
        entryMap[f.replace(/\.js$/, '')] = ['./src/main/js/' + f]
    })

module.exports = {
    entry: entryMap,
    output: {
        filename: 'bundle-[name].js',
        path: path.resolve(__dirname, 'src/main/resources/static/js')
    },
    mode: 'development',
    devtool: 'source-map',
    resolve: {
        extensions: ['.js']
    },
    module: {
        rules: [
            {
                test: /\.s?css$/i,
                use: [MiniCssExtractPlugin.loader, "css-loader", "sass-loader"],
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/i,
                type: "asset",
                generator: {
                    filename: '../fonts/[hash][ext][query]'
                }
            },
            {
                test: /\.(png|svg|jpg|jpeg|gif)$/i,
                type: "asset",
            },
            {
                enforce: 'pre',
                test: /\.js$/,
                loader: 'source-map-loader'
            }
        ]
    },
    plugins: [
        new MiniCssExtractPlugin({
            filename: '../css/bundle.css'
        })
    ]
}
