import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import Backend from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';
import commonDa from './i18n/locales/da/translation.json'
import commonEn from './i18n/locales/en/translation.json'
// don't want to use this?
// have a look at the Quick start guide
// for passing in lng and translations on init

const resources = {
    da: { common: commonDa },
    en: { common: commonEn },
}

const options = {
    order: ['querystring', 'navigator'],
    lookupQuerystring: 'lng'
}
i18n
    // load translation using http -> see /public/locales (i.e. https://github.com/i18next/react-i18next/tree/master/example/react/public/locales)
    // learn more: https://github.com/i18next/i18next-http-backend
    // want your translations to be loaded from a professional CDN? => https://github.com/locize/react-tutorial#step-2---use-the-locize-cdn
    .use(Backend)
    // detect user language
    // learn more: https://github.com/i18next/i18next-browser-languageDetector
    .use(LanguageDetector)
    // pass the i18n instance to react-i18next.
    .use(initReactI18next)
    // init i18next
    // for all options read: https://www.i18next.com/overview/configuration-options
    .init({
        fallbackLng: "en",
        supportedLngs: ["en", "da"],
        ns: ["common", "home"],
        defaultNS: "common",
        backend: { loadPath: "/i18n/locales/{{lng}}/{{ns}}.json" },
        detection: options,
        resources,
        interpolation: { escapeValue: false },
        react: { useSuspense: true }
    });


export default i18n;