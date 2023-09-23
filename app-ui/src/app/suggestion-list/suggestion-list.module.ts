import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { SuggestionListComponent } from './suggestion-list.component';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';


@NgModule({
  declarations: [
    SuggestionListComponent
  ],
  imports: [
    CommonModule,
    MatTableModule
  ],
  providers: [],
})
export class SuggestionListModule { }
