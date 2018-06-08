import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppSharedModule } from 'app/shared';
import {
    BlacklistService,
    BlacklistComponent,
    BlacklistDetailComponent,
    BlacklistUpdateComponent,
    BlacklistDeletePopupComponent,
    BlacklistDeleteDialogComponent,
    blacklistRoute,
    blacklistPopupRoute,
    BlacklistResolve,
    BlacklistResolvePagingParams,
    // DataFile Import support
    DataFileService,
    DataFilePopupService,
    DataFileDialogComponent,
    DataFilePopupComponent
} from './';

const ENTITY_STATES = [...blacklistRoute, ...blacklistPopupRoute];

@NgModule({
    imports: [AppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BlacklistComponent,
        BlacklistDetailComponent,
        BlacklistUpdateComponent,
        BlacklistDeleteDialogComponent,
        BlacklistDeletePopupComponent,
        DataFileDialogComponent,
        DataFilePopupComponent
    ],
    entryComponents: [
        BlacklistComponent,
        BlacklistUpdateComponent,
        BlacklistDeleteDialogComponent,
        BlacklistDeletePopupComponent,
        DataFileDialogComponent,
        DataFilePopupComponent
    ],
    providers: [BlacklistService, BlacklistResolve, BlacklistResolvePagingParams, DataFileService, DataFilePopupService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppBlacklistModule {}
