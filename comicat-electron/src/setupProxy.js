const {createProxyMiddleware} = require('http-proxy-middleware');
module.exports = function (app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'http://localhost:47373/api/v1',
            changeOrigin: true,
            pathRewrite: {
                '^/api': '',
            }
        })
    );
    app.use(
        '/apc',
        createProxyMiddleware({
            target: 'https://api.inews.qq.com/',
            changeOrigin: true,
            pathRewrite: {
                '^/apc': '',
            }
        })
    );
}
