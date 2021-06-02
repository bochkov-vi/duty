module.exports = {

    runtimeCompiler: true,
    pluginOptions: {
        i18n: {
            locale: 'en',
            fallbackLocale: 'en',
            localeDir: 'locales',
            enableInSFC: true
        }
    },
    transpileDependencies: [
        'vuetify'
    ],

    devServer: {

        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    }

}
