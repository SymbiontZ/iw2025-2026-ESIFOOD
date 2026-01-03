import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_0 from 'Frontend/styles/producto-components.css?inline';
import $cssFromFile_1 from 'Frontend/styles/style.css?inline';
import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import '@vaadin/login/theme/lumo/vaadin-login-form.js';
import '@vaadin/notification/theme/lumo/vaadin-notification.js';
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
  if (key === '16216400d8445f4b9698e11d1cc68c1e4b0158bbcb08019bb63ff8bdec22a6e2') {
    pending.push(import('./chunks/chunk-38acd6f503d0458985ba1a249b1dfaac116dfb65345d0429aea141cb9161fdd6.js'));
  }
  if (key === '1778aac2276440136123ce1de9125f383f988b78d1a27f2f0deb84562408c449') {
    pending.push(import('./chunks/chunk-59c7c253d6b474447a32154c2639833b12f694513c3bdc869d35137b8bf4cb49.js'));
  }
  if (key === '9f17696fdece55a32428d7f1b537eee5568be27a416f3e3708a6337eb2605121') {
    pending.push(import('./chunks/chunk-ca40e1fae63a733f932c5de7ddca0fba38a3d0f5327858c60e5ac2a3a9f9d9fd.js'));
  }
  if (key === 'e527190118bf433c8c8300418a06eb2148851646bb217a8fc8b696bcf7d0d144') {
    pending.push(import('./chunks/chunk-6933246967070768d9d96e92714c6af701f86a92c7fc6a2882922d19d6f813e3.js'));
  }
  if (key === '1b6fe4d60887fd662250e7cb43bdc601f395d425b167b03880d466dcad30ddf8') {
    pending.push(import('./chunks/chunk-961f59352a6dd7e49b1bff9e95568c58b60b62ad96dc5dbf724200740920326e.js'));
  }
  if (key === 'e57dafc9f2bf0257b290547a8c08ca0f1f107813a40d07a95ccaf14bb1863a21') {
    pending.push(import('./chunks/chunk-0a4a0c57b9166704a11028be689a54a41683457a41e4da56d86de50fabef2aa0.js'));
  }
  if (key === '3ea9d7a5c9859de2b218cb1e0e56af5ce6ee3fe08ecc53b848d7e55611503144') {
    pending.push(import('./chunks/chunk-961f59352a6dd7e49b1bff9e95568c58b60b62ad96dc5dbf724200740920326e.js'));
  }
  if (key === '793b7d784e01d39cfeedd3563022f08f60a27a3113a4f4b114eb5e000bdff5e5') {
    pending.push(import('./chunks/chunk-d289eb9dc5e32f82430ddfa5837695da363a68b50a7b5215344641474e05b23f.js'));
  }
  if (key === '722f2e6da6f38f145e17b3ea744dae47ad9989dcb54d575a32e61466411b1383') {
    pending.push(import('./chunks/chunk-c94d0fe70c79f08dfaadd9f54e6a48c70adfc2b2c4ef5cae417360abc897192b.js'));
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