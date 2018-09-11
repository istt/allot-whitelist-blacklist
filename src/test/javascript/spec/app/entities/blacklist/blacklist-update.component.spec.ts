/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AppTestModule } from '../../../test.module';
import { BlacklistUpdateComponent } from 'app/entities/blacklist/blacklist-update.component';
import { BlacklistService } from 'app/entities/blacklist/blacklist.service';
import { Blacklist } from 'app/shared/model/blacklist.model';

describe('Component Tests', () => {
    describe('Blacklist Management Update Component', () => {
        let comp: BlacklistUpdateComponent;
        let fixture: ComponentFixture<BlacklistUpdateComponent>;
        let service: BlacklistService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AppTestModule],
                declarations: [BlacklistUpdateComponent]
            })
                .overrideTemplate(BlacklistUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BlacklistUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BlacklistService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Blacklist(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.blacklist = entity;
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
                    const entity = new Blacklist();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.blacklist = entity;
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
