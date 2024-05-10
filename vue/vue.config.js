module.exports= {
    devServer: {
        port: 8083, // 启动端口
        open: false,  // 启动后是否自动打开网页
        proxy: {
            '/api': {
                target: 'https://view.inews.qq.com', //要跨域的域名
                secure: true,//接受对方是https的接口
                ws: true, // 是否启用websockets
                changeOrigin: true, //是否允许跨越
                pathRewrite: {
                    '^/api': '/'  //将你的地址代理位这个 /api 接下来请求时就使用这个/api来代替你的地址
                },
            },
            '/aki': {
                target: 'https://api.inews.qq.com',
                secure: true,
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/aki': '/'
                },
            }

        },

    }
}

