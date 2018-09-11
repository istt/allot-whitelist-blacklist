import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppSharedModule } from 'app/shared';
import {
    WhitelistService,
    WhitelistComponent,
    WhitelistDetailComponent,
    WhitelistUpdateComponent,
    WhitelistDeletePopupComponent,
    WhitelistDeleteDialogComponent,
    whitelistRoute,
    whitelistPopupRoute,
    WhitelistResolve,
    WhitelistResolvePagingParams
} from './';

const ENTITY_STATES = [...whitelistRoute, ...whitelistPopupRoute];

@NgModule({
    imports: [AppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WhitelistComponent,
        WhitelistDetailComponent,
        WhitelistUpdateComponent,
        WhitelistDeleteDialogComponent,
        WhitelistDeletePopupComponent
    ],
    entryComponents: [WhitelistComponent, WhitelistUpdateComponent, WhitelistDeleteDialogComponent, WhitelistDeletePopupComponent],
    providers: [WhitelistService, WhitelistResolve, WhitelistResolvePagingParams],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppWhitelistModule {}
