package ua.com.wl.archetype.core.di.dagger.factory;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import androidx.lifecycle.ViewModel;

import dagger.MapKey;

/**
 * @author Denis Makovskyi
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MapKey
@Documented
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}
