import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AppWhitelistModule } from './whitelist/whitelist.module';
import { AppBlacklistModule } from './blacklist/blacklist.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        AppWhitelistModule,
        AppBlacklistModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppEntityModule {}
