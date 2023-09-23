import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RedditorComponent } from './redditor.component';
import { FormsModule } from '@angular/forms';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatButtonModule} from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';


@NgModule({
  declarations: [
    RedditorComponent,
  ],
  imports: [
    FormsModule,
    MatFormFieldModule, 
    MatProgressSpinnerModule,
    MatInputModule, MatSelectModule,
    MatButtonModule,
    BrowserAnimationsModule
  ],
  providers: [],
})
export class RedditorModule { }
