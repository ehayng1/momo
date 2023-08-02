module.exports = {
  root: true,

  env: {
    node: true
  },

  extends: [
    'plugin:vue/essential',
    'eslint:recommended',
    // "@vue/typescript/recommended",
    '@vue/prettier'
    // "@vue/prettier/@typescript-eslint",
  ],
  parserOptions: {
    // ecmaVersion: 2020,
    parser: 'babel-eslint'
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'prettier/prettier': [
      'error',
      {
        useTabs: false,
        tabWidth: 2,
        printWidth: 80,
        bracketSpacing: true,
        arrowParens: 'avoid',
        trailingComma: 'none',
        singleQuote: true,
        semi: false,
        vueIndentScriptAndStyle: true
      }
    ]
  }
}
