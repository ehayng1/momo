import Vue from 'vue'
import VueSanitize from 'vue-sanitize'

/*
 * https://github.com/apostrophecms/sanitize-html#default-options
 * */

const defaultOptions = {
  allowedTags: [
    'img',
    'address',
    'article',
    'aside',
    'footer',
    'header',
    'h1',
    'h2',
    'h3',
    'h4',
    'h5',
    'h6',
    'hgroup',
    'main',
    'nav',
    'section',
    'blockquote',
    'dd',
    'div',
    'dl',
    'dt',
    'figcaption',
    'figure',
    'hr',
    'li',
    'main',
    'ol',
    'p',
    'pre',
    'ul',
    'a',
    'abbr',
    'b',
    'bdi',
    'bdo',
    'br',
    'cite',
    'code',
    'data',
    'dfn',
    'em',
    'i',
    'kbd',
    'mark',
    'q',
    'rb',
    'rp',
    'rt',
    'rtc',
    'ruby',
    's',
    'samp',
    'small',
    'span',
    'strong',
    'sub',
    'sup',
    'time',
    'u',
    'var',
    'wbr',
    'caption',
    'col',
    'colgroup',
    'table',
    'tbody',
    'td',
    'tfoot',
    'th',
    'thead',
    'tr'
  ],
  disallowedTagsMode: 'discard',
  allowedAttributes: {
    a: ['href', 'class', 'style', 'data-card-branding', 'download', 'target'],
    span: ['style', 'class'],
    p: ['style', 'class'],
    div: ['style', 'class', 'data-original-embed'],
    img: ['src', 'style', 'class'],
    table: ['style', 'class'],
    th: ['colspan', 'rowspan', 'style', 'class'],
    td: ['colspan', 'rowspan', 'style', 'class'],
    iframe: [
      'src',
      'class',
      'style',
      'frameborder',
      'allowfullscreen',
      'width',
      'height',
      'title',
      'allow'
    ],
    h1: ['style', 'class'],
    h2: ['style', 'class'],
    h3: ['style', 'class'],
    h4: ['style', 'class'],
    h5: ['style', 'class'],
    h6: ['style', 'class'],
    code: ['style', 'class'],
    ul: ['style', 'class'],
    ol: ['style', 'class'],
    li: ['style', 'class']
  },
  // Lots of these won't come up by default because we don't allow them
  selfClosing: [
    'img',
    'br',
    'hr',
    'area',
    'base',
    'basefont',
    'input',
    'link',
    'meta'
  ],
  // URL schemes we permit
  allowedSchemes: ['http', 'https', 'ftp', 'mailto', 'tel'],
  allowedSchemesByTag: {},
  allowedSchemesAppliedToAttributes: ['href', 'src', 'cite'],
  allowProtocolRelative: true,
  enforceHtmlBoundary: false
}
Vue.use(VueSanitize, defaultOptions)