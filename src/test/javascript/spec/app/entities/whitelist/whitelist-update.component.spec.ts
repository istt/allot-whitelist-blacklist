/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { AppTestModule } from '../../../test.module';
import { WhitelistUpdateComponent } from 'app/entities/whitelist/whitelist-update.component';
import { WhitelistService } from 'app/entities/whitelist/whitelist.service';
import { Whitelist } from 'app/shared/model/whitelist.model';

describe('Component Tests', () => {
    describe('Whitelist Management Update Component', () => {
        let comp: WhitelistUpdateComponent;
        let fixture: ComponentFixture<WhitelistUpdateComponent>;
        let service: WhitelistService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AppTestModule],
                declarations: [WhitelistUpdateComponent],
                providers: [WhitelistService]
            })
                .overrideTemplate(WhitelistUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WhitelistUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WhitelistService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Whitelist(123);
                    spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                    comp.whitelist = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Whitelist();
                    spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({ body: entity })));
                    comp.whitelist = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
