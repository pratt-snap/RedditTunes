import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RedditorComponent } from './redditor/redditor.component';
import { SuggestionListComponent } from './suggestion-list/suggestion-list.component';

const routes: Routes = [
  // ... Other routes
  { path: 'redditor', component: RedditorComponent },
  {path:'suggestions', component:SuggestionListComponent} 
  // ... Other routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
