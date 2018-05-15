/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.users;

/**
 * Represents the distinct types of users: User and admin.
 */
public enum UserType {
    USER(1), ADMIN(0);

    private final int num;

    UserType(int num) {
        this.num = num;
    }

    public static UserType fromInt(int num) {
        if (num == 0) {
            return ADMIN;
        } else {
            return USER;
        }
    }
}
