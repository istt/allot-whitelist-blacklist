import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppSharedModule } from 'app/shared';
import {
    WhitelistComponent,
    WhitelistDetailComponent,
    WhitelistUpdateComponent,
    WhitelistDeletePopupComponent,
    WhitelistDeleteDialogComponent,
    whitelistRoute,
    WhitelistService,
    whitelistPopupRoute,
    WhitelistResolve,
    // DataFile Import support
    DataFileService,
    DataFilePopupService,
    DataFileDialogComponent,
    DataFilePopupComponent
} from './';

const ENTITY_STATES = [...whitelistRoute, ...whitelistPopupRoute];

@NgModule({
    imports: [AppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WhitelistComponent,
        WhitelistDetailComponent,
        WhitelistUpdateComponent,
        WhitelistDeleteDialogComponent,
        WhitelistDeletePopupComponent,
        DataFileDialogComponent,
        DataFilePopupComponent
    ],
    entryComponents: [
        WhitelistComponent,
        WhitelistUpdateComponent,
        WhitelistDeleteDialogComponent,
        WhitelistDeletePopupComponent,
        DataFileDialogComponent,
        DataFilePopupComponent
    ],
    providers: [WhitelistService, WhitelistResolve, DataFileService, DataFilePopupService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppWhitelistModule {}
