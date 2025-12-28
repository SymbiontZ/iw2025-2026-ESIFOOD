import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_0 from 'Frontend/styles/producto-components.css?inline';
import $cssFromFile_1 from 'Frontend/styles/style.css?inline';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/button/src/vaadin-button.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/login/src/vaadin-login-form.js';
import '@vaadin/notification/src/vaadin-notification.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

injectGlobalCss($cssFromFile_0.toString(), 'CSSImport end', document);
injectGlobalWebcomponentCss($cssFromFile_0.toString());

injectGlobalCss($cssFromFile_1.toString(), 'CSSImport end', document);
injectGlobalWebcomponentCss($cssFromFile_1.toString());

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '1778aac2276440136123ce1de9125f383f988b78d1a27f2f0deb84562408c449') {
    pending.push(import('./chunks/chunk-c455a3ff365be340d042f8b82029ed32846cbe7ccd28e0796787b733d0671303.js'));
  }
  if (key === '9f17696fdece55a32428d7f1b537eee5568be27a416f3e3708a6337eb2605121') {
    pending.push(import('./chunks/chunk-c1a001389cf49202a1a9a5c6a5bf9a153f18b3901339a7607130d29dd5353412.js'));
  }
  if (key === '16216400d8445f4b9698e11d1cc68c1e4b0158bbcb08019bb63ff8bdec22a6e2') {
    pending.push(import('./chunks/chunk-4cbd628b534ee8ccb82c4b4cef00d4e75123ac835520e496537ddf7cf69a23e0.js'));
  }
  if (key === 'e527190118bf433c8c8300418a06eb2148851646bb217a8fc8b696bcf7d0d144') {
    pending.push(import('./chunks/chunk-90c86f689be6d931ce6c03babb29b949132ccf11166ee5f390cf7f96dcfe994d.js'));
  }
  if (key === 'e57dafc9f2bf0257b290547a8c08ca0f1f107813a40d07a95ccaf14bb1863a21') {
    pending.push(import('./chunks/chunk-531c5da3dab69eff28d7802370c68e94654be94ed121b32830017628dce4375a.js'));
  }
  if (key === '3ea9d7a5c9859de2b218cb1e0e56af5ce6ee3fe08ecc53b848d7e55611503144') {
    pending.push(import('./chunks/chunk-eae3f977b6888513efaf4c4036d2a087b1bc7aa40a464d28aaf6f4e950f2135b.js'));
  }
  if (key === '793b7d784e01d39cfeedd3563022f08f60a27a3113a4f4b114eb5e000bdff5e5') {
    pending.push(import('./chunks/chunk-ff9a531ee06e8c85423fa4e5e5f5860bd7035a1f58888166a1929e80d92a5883.js'));
  }
  if (key === '722f2e6da6f38f145e17b3ea744dae47ad9989dcb54d575a32e61466411b1383') {
    pending.push(import('./chunks/chunk-11fd1f9009fce809b34fd1d3d1bf9866f81a5a0b22cd656797ae7440c6a83756.js'));
  }
  if (key === '1b6fe4d60887fd662250e7cb43bdc601f395d425b167b03880d466dcad30ddf8') {
    pending.push(import('./chunks/chunk-eae3f977b6888513efaf4c4036d2a087b1bc7aa40a464d28aaf6f4e950f2135b.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}