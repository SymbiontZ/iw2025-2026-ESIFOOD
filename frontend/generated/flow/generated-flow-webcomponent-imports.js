import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_0 from 'Frontend/styles/producto-components.css?inline';
import $cssFromFile_1 from 'Frontend/styles/style.css?inline';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/icon/theme/lumo/vaadin-icon.js';
import '@vaadin/context-menu/theme/lumo/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import 'Frontend/generated/jar-resources/menubarConnector.js';
import '@vaadin/menu-bar/theme/lumo/vaadin-menu-bar.js';
import '@vaadin/notification/theme/lumo/vaadin-notification.js';
import '@vaadin/login/theme/lumo/vaadin-login-form.js';
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
    pending.push(import('./chunks/chunk-59c7c253d6b474447a32154c2639833b12f694513c3bdc869d35137b8bf4cb49.js'));
  }
  if (key === '16216400d8445f4b9698e11d1cc68c1e4b0158bbcb08019bb63ff8bdec22a6e2') {
    pending.push(import('./chunks/chunk-320aa2e61b0fab23b36f58f3c820d16302ae06657454fcc20a33e4edd9c3a5de.js'));
  }
  if (key === '722f2e6da6f38f145e17b3ea744dae47ad9989dcb54d575a32e61466411b1383') {
    pending.push(import('./chunks/chunk-c6e856308bd7a8487c0969682d922711626b93e1fcd42921f6a102ed2818dad5.js'));
  }
  if (key === '9326bb279b4070ede79f0a0591cf3325f5f59f3f59df5bfd1179aa0a207b9fbe') {
    pending.push(import('./chunks/chunk-8109584c96e9685a8d202a93f86a1bd229ff9eb9a9dae89e9335d01d92513b41.js'));
  }
  if (key === '1b6fe4d60887fd662250e7cb43bdc601f395d425b167b03880d466dcad30ddf8') {
    pending.push(import('./chunks/chunk-11a85b106c0fcb6436e5e4001753f82b57bd03380a6bc9eddda61d0d94a64585.js'));
  }
  if (key === 'e527190118bf433c8c8300418a06eb2148851646bb217a8fc8b696bcf7d0d144') {
    pending.push(import('./chunks/chunk-d209a7b81fb1b6c18171febedb3d258244aac42562d0abfe0c816eda243eab95.js'));
  }
  if (key === '3ea9d7a5c9859de2b218cb1e0e56af5ce6ee3fe08ecc53b848d7e55611503144') {
    pending.push(import('./chunks/chunk-320aa2e61b0fab23b36f58f3c820d16302ae06657454fcc20a33e4edd9c3a5de.js'));
  }
  if (key === '793b7d784e01d39cfeedd3563022f08f60a27a3113a4f4b114eb5e000bdff5e5') {
    pending.push(import('./chunks/chunk-17d2197517b43884e93c9029e328cbd3b900f5d90e011e3dfe59e39039eb0244.js'));
  }
  if (key === 'e57dafc9f2bf0257b290547a8c08ca0f1f107813a40d07a95ccaf14bb1863a21') {
    pending.push(import('./chunks/chunk-f94368c1fd7344a745ae72cb2f5d8d10b76e9fdb5bc635dcb962d00e1881acd8.js'));
  }
  if (key === '9f17696fdece55a32428d7f1b537eee5568be27a416f3e3708a6337eb2605121') {
    pending.push(import('./chunks/chunk-dfac41c5f57c1ddf9dce16fcd50f4043ac78de7be7be7940c54d6a307d6296ce.js'));
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