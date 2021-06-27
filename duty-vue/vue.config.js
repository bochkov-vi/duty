process.env.VUE_APP_DYN_VALUE="asdfg"
const ip = require("ip");
console.dir ( ip.address() );
process.env.VUE_APP_REST_IP=ip.address()

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
