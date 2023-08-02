module.exports = {
  runtimeCompiler: true,
  publicPath: process.env.VUE_APP_IMAGE_DOMAIN_HOST,
  outputDir: './dist/resources/',
  assetsDir: './static/',
  chainWebpack: config => {
    console.log(config)
    const svgRule = config.module.rule('svg')
    svgRule.uses.clear()
    svgRule
      .oneOf('external')
      .resourceQuery(/ext/)
      .use('file-loader')
      .loader('file-loader')
      .options({
        name: 'static/svg/[name].[ext]?[hash:8]'
      })
      .end()
      .end()
      .oneOf('internal')
      .use('vue-svg-loader')
      .loader('vue-svg-loader')
      .end()

    const fontRule = config.module.rule('fonts')
    fontRule.uses.clear()
    fontRule
      .test(/\.(woff2?|eot|ttf|otf)(\?.*)?$/)
      .use('url-loader')
      .loader('url-loader')
      .options({
        name: 'static/fonts/[name].[ext]?[hash:8]',
        limit: 10000
      })
      .end()

    const imageRule = config.module.rule('images')
    imageRule
      .test(/\.(png|jpe?g|gif|ico|webp)(\?.*)?$/)
      .use('url-loader')
      .loader('url-loader')
      .options({
        name: 'static/images/[name].[ext]?[hash:8]'
      })
      .end()
    // .tap((options) => {
    //   options.limit = -1
    //   options.outputPath = './assets/fonts'
    //   options.fallback.options.name = '[path][name].[ext]?[hash:8]'
    //   return options
    // })
  },
  devServer: {
    // 로컬
    // outputDir: '../server/src/main/resources/static',
    // // outputDir: '.././server/src/main/resources/static', // 빌드 타겟 디렉토리
    // devServer: {
    //   proxy: {
    //     '/api': {
    //       target: 'http://localhost:8088',
    //       changeOrigin: true // cross origin 허용
    //     }
    //   },

    //   proxy: {
    //     '/api': {
    //       target: 'http://localhost:8088/'
    //     },
    //   '/oauth2.0': {
    //     target: 'https://nid.naver.com/',
    //     changeOrigin: true,
    //     logLevel: 'debug',
    //     ws: false
    //   },
    //   '/v1': {
    //     target: 'https://openapi.naver.com/',
    //     changeOrigin: true,
    //     logLevel: 'debug',
    //     ws: false
    //   }
    // },
    // 로컬 끝
    // 서버
    proxy: {
      '/api': {
        // target: 'https://api.momobuild.com/', // 테스트서버
        target: 'https://api.buildmomo.com/', // 실서버
        // pathRewrite: { '^/': '' },
        changeOrigin: true,
        secure: false
      },
      '/oauth2.0': {
        target: 'https://nid.naver.com/',
        changeOrigin: true,
        logLevel: 'debug',
        ws: false
      },
      '/v1': {
        target: 'https://openapi.naver.com/',
        changeOrigin: true,
        logLevel: 'debug',
        ws: false
      }
    },
    // 서버 끝
    port: 3015,
    // https: true, // webshare 테스트용
    historyApiFallback: true // 빈 페이지?
  },
  css: {
    sourceMap: process.env.NODE_ENV !== 'production',
    loaderOptions: {
      sass: {
        additionalData: `
          @import "~@/static/scss/common/_function.scss";
          @import "~@/static/scss/common/_variable.scss";
        `
      }
    }
  }
}
