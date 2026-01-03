import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/icon/theme/lumo/vaadin-icon.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/notification/theme/lumo/vaadin-notification.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_0 from 'Frontend/styles/producto-components.css?inline';

injectGlobalCss($cssFromFile_0.toString(), 'CSSImport end', document);
import $cssFromFile_1 from 'Frontend/styles/style.css?inline';

injectGlobalCss($cssFromFile_1.toString(), 'CSSImport end', document);