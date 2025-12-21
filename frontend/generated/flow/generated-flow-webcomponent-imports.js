import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_0 from 'Frontend/styles/producto-components.css?inline';
import $cssFromFile_1 from 'Frontend/styles/style.css?inline';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import '@vaadin/login/theme/lumo/vaadin-login-form.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
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
  if (key === '3ea9d7a5c9859de2b218cb1e0e56af5ce6ee3fe08ecc53b848d7e55611503144') {
    pending.push(import('./chunks/chunk-961f59352a6dd7e49b1bff9e95568c58b60b62ad96dc5dbf724200740920326e.js'));
  }
  if (key === '793b7d784e01d39cfeedd3563022f08f60a27a3113a4f4b114eb5e000bdff5e5') {
    pending.push(import('./chunks/chunk-193f5ed0e3315de7f95341217ee386f2e46945767240f7dbc7946fa66f36d335.js'));
  }
  if (key === '9f17696fdece55a32428d7f1b537eee5568be27a416f3e3708a6337eb2605121') {
    pending.push(import('./chunks/chunk-9b9c0502d689b239b9f1eb0bdd40255b753fe64097dd63bf2a2ddb7018e2da9e.js'));
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