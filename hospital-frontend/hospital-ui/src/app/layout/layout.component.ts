import { Component } from '@angular/core';
import { MenuService, MenuItem } from '../core/services/menu.service';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent {
  menuItems: MenuItem[] = [];

  constructor(private menuService: MenuService) {
    this.menuItems = this.menuService.getMenuItems();
  }
}


