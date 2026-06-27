// i18next-scanner.config.js
module.exports = {
    input: [
        'src/**/*.{ts,tsx}', // Scan all TypeScript files
        '!**/node_modules/**'
    ],
    output: './src/i18n', // Where translation JSON files will be stored
    options: {
        debug: false,
        removeUnusedKeys: true,
        sort: true,
        func: {
            list: ['t'], // Functions to look for
            extensions: ['.ts', '.tsx']
        },
        lngs: ['en', 'da'], // Languages
        defaultLng: 'en',
        defaultNs: 'translation',
        resource: {
            loadPath: 'locales/{{lng}}/{{ns}}.json',
            savePath: 'locales/{{lng}}/{{ns}}.json'
        },
        keySeparator: false, // Allow full sentences as keys
        nsSeparator: false
    }
};
